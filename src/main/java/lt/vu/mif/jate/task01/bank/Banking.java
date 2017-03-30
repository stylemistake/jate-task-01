package lt.vu.mif.jate.task01.bank;

import lt.vu.mif.jate.task01.bank.exception.BankNotFoundException;
import lt.vu.mif.jate.task01.bank.exception.IBANException;
import lt.vu.mif.jate.task01.bank.exception.WrongAccountTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Banking service.
 */
public class Banking {

    /**
     * Banking service instance.
     */
    private static Banking instance;

    /**
     * Retrieve service instance.
     * @return Instance
     */
    public static synchronized Banking getInstance() {
        if (instance == null) {
            instance = new Banking();
        }
        return instance;
    }

    /**
     * Map, holding banks, nested into separate maps for each country code.
     */
    private Map<String, Map<Integer, Bank>> banks = new HashMap<>();

    /**
     * Map, holding all bank accounts.
     */
    private Map<String, Account> accounts = new HashMap<>();

    /**
     * A magic number that defies human intelligence.
     */
    private static final int HOCUS_POCUS = 3;

    /**
     * Constructor.
     */
    Banking() {
        // Pre-define locales in banks
        Map<Integer, Bank> banksLt = new HashMap<>();
        banks.put("LT", banksLt);
        // Read banks from file
        ArrayList<String[]> lines = Util.readResourceFileCSV(
            "banking/banks.txt", ":");
        for (String[] line: lines) {
            int code = Integer.parseInt(line[HOCUS_POCUS]);
            Bank bank = new Bank("LT", code, line[2], line[0], line[1]);
            banksLt.put(code, bank);
        }
    }

    /**
     * Get all banks.
     * @return Bank map
     */
    public final Map<String, Map<Integer, Bank>> getBanks() {
        Map<String, Map<Integer, Bank>> copy = new HashMap<>();
        for (Map.Entry<String, Map<Integer, Bank>> entry: banks.entrySet()) {
            copy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Get a specific bank object.
     * @param country Country code
     * @param code Bank code
     * @return Bank object
     * @throws BankNotFoundException Not found
     */
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

    /**
     * Same as getBank, but instead of throwing an exception, creates the
     * bank if it doesn't exist.
     * @param country Country code
     * @param code Bank code
     * @return Bank object
     */
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

    /**
     * Get a bank account of type CurrentAccount.
     * @param iban IBAN number
     * @return CurrentAccount
     * @throws IBANException Wrong IBAN
     * @throws WrongAccountTypeException Account exists and has other type
     */
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

    /**
     * Get a bank account of type CreditAccount.
     * @param iban IBAN number
     * @return CreditAccount
     * @throws IBANException Wrong IBAN
     * @throws WrongAccountTypeException Account exists and has other type
     */
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

    /**
     * Get a bank account of type SavingsAccount.
     * @param iban IBAN number
     * @return SavingsAccount
     * @throws IBANException Wrong IBAN
     * @throws WrongAccountTypeException Account exists and has other type
     */
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

    /**
     * Returns a converter service.
     * @return Converter service
     */
    public final Converter getConverter() {
        return Converter.getInstance();
    }

}
