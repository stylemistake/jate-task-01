package lt.vu.mif.jate.task01.bank;

import lt.vu.mif.jate.task01.bank.exception.IBANException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

final class IBANParser {

    private IBANParser() { }

    private static HashMap<String, String> ibanRules;

    private static synchronized HashMap<String, String> getIBANRules() {
        if (ibanRules != null) {
            return ibanRules;
        }
        ibanRules = new HashMap<>();
        ArrayList<String[]> lines = Util.readResourceFileCSV(
            "banking/iban.txt", ":");
        for (String[] line: lines) {
            String ibanRule = line[1].replace(" ", "");
            String country = ibanRule
                .substring(0, 2)
                .toUpperCase(Locale.ENGLISH);
            ibanRules.put(country, ibanRule);
        }
        return ibanRules;
    }

    private static String extractStringAtChar(final String value,
                                              final String rule,
                                              final String chr) {
        return value.substring(rule.indexOf(chr), rule.lastIndexOf(chr) + 1);
    }

    static String normalize(final String iban) {
        return iban.replace(" ", "").toUpperCase(Locale.ENGLISH);
    }

    static IBAN parse(final String iban) throws IBANException {
        String normalized = normalize(iban);
        String country = normalized.substring(0, 2);
        if (!getIBANRules().containsKey(country)) {
            throw new IBANException(normalized, "IBAN country not found: "
                + country);
        }
        String rule = getIBANRules().get(country);
        if (rule.length() != normalized.length()) {
            throw new IBANException(normalized, "IBAN number length wrong: "
                + "expected " + rule.length()
                + ", got " + normalized.length());
        }
        IBAN parsedIban;
        try {
            int bankCode = Integer.parseInt(extractStringAtChar(normalized,
                rule, "b"));
            BigInteger accountNumber = new BigInteger(extractStringAtChar(
                normalized, rule, "c"));
            parsedIban = new IBAN(normalized, country, bankCode, accountNumber);
        } catch (NumberFormatException e) {
            throw new IBANException(normalized, "IBAN format wrong: "
                + normalized);
        }
        return parsedIban;
    }

}
