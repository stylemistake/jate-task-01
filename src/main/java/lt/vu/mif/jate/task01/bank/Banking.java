package lt.vu.mif.jate.task01.bank;

import lt.vu.mif.jate.task01.bank.exception.BankNotFoundException;
import lt.vu.mif.jate.task01.bank.exception.IBANException;
import lt.vu.mif.jate.task01.bank.exception.WrongAccountTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Banking {

    private static Banking instance;

    public static synchronized Banking getInstance() {
        if (instance == null) {
            instance = new Banking();
        }
        return instance;
    }

    private Map<String, Map<Integer, Bank>> banks = new HashMap<>();
    private Map<String, Account> accounts = new HashMap<>();

    Banking() {
        // Pre-define locales in banks
        Map<Integer, Bank> banksLt = new HashMap<>();
        banks.put("LT", banksLt);
        // Read banks from file
        ArrayList<String[]> lines = Util.readResourceFileCSV(
            "banking/banks.txt", ":");
        for (String[] line: lines) {
            int code = Integer.parseInt(line[3]);
            Bank bank = new Bank("LT", code, line[2], line[0], line[1]);
            banksLt.put(code, bank);
        }
    }

    public final Map<String, Map<Integer, Bank>> getBanks() {
        Map<String, Map<Integer, Bank>> copy = new HashMap<>();
        for (Map.Entry<String, Map<Integer, Bank>> entry: banks.entrySet()) {
            copy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return copy;
    }

    public final Bank getBank(final String country, final Integer code)
            throws BankNotFoundException {
        if (!banks.containsKey(country)) {
            throw new BankNotFoundException(country, code);
        }
        Bank bank = banks.get(country).get(code);
        if (bank == null) {
            throw new BankNotFoundException(country, code);
        }
        return bank;
    }

    final Bank getBankOrCreate(final String country, final Integer code) {
        try {
            return getBank(country, code);
        } catch (BankNotFoundException e) {
            Bank bank = new Bank(country, code);
            banks.putIfAbsent(country, new HashMap<>());
            banks.get(country).put(code, bank);
            return bank;
        }
    }

    public final CurrentAccount getCurrentAccount(final String iban)
            throws IBANException, WrongAccountTypeException {
        String normalized = IBANParser.normalize(iban);
        if (accounts.containsKey(normalized)) {
            Account account = accounts.get(normalized);
            if (!(account instanceof CurrentAccount)) {
                throw new WrongAccountTypeException("Account type was "
                    + account.getClass().getSimpleName());
            }
            return (CurrentAccount) account;
        }
        CurrentAccount account = new CurrentAccount(normalized);
        accounts.put(normalized, account);
        return account;
    }

    public final CreditAccount getCreditAccount(final String iban)
            throws IBANException, WrongAccountTypeException {
        String normalized = IBANParser.normalize(iban);
        if (accounts.containsKey(normalized)) {
            Account account = accounts.get(normalized);
            if (!(account instanceof CreditAccount)) {
                throw new WrongAccountTypeException("Account type was "
                    + account.getClass().getSimpleName());
            }
            return (CreditAccount) account;
        }
        CreditAccount account = new CreditAccount(normalized);
        accounts.put(normalized, account);
        return account;
    }

    public final SavingsAccount getSavingsAccount(final String iban)
            throws IBANException, WrongAccountTypeException {
        String normalized = IBANParser.normalize(iban);
        if (accounts.containsKey(normalized)) {
            Account account = accounts.get(normalized);
            if (!(account instanceof SavingsAccount)) {
                throw new WrongAccountTypeException("Account type was "
                    + account.getClass().getSimpleName());
            }
            return (SavingsAccount) account;
        }
        SavingsAccount account = new SavingsAccount(normalized);
        accounts.put(normalized, account);
        return account;
    }

    public final Converter getConverter() {
        return Converter.getInstance();
    }
}
