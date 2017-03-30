package lt.vu.mif.jate.task01.bank;

import lt.vu.mif.jate.task01.bank.exception.IBANException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * IBAN Parser and IBAN object factory.
 */
final class IBANParser {

    /**
     * Placeholder.
     */
    private IBANParser() { }

    /**
     * Map, which stores contents of iban.txt.
     */
    private static HashMap<String, String> ibanRules;

    /**
     * Returns contents of ibanRules in lazy and cached manner.
     * @return IBAN rules
     */
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

    /**
     * Extracts a string by pattern matching against the IBAN rule.
     * @param value String to extract from
     * @param rule String with the pattern
     * @param chr Character, denoting the pattern we need to get
     * @return Pattern-matched string
     */
    private static String extractStringAtChar(final String value,
            final String rule, final String chr) {
        return value.substring(rule.indexOf(chr), rule.lastIndexOf(chr) + 1);
    }

    /**
     * Normalizes the IBAN string (trims spaces and does uppercase).
     * @param iban IBAN string
     * @return Normalized string
     */
    static String normalize(final String iban) {
        return iban.replace(" ", "").toUpperCase(Locale.ENGLISH);
    }

    /**
     * Parse an IBAN string to get an IBAN object.
     * @param iban IBAN string
     * @throws IBANException Country not found, wrong length or format
     * @return IBAN object
     */
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
