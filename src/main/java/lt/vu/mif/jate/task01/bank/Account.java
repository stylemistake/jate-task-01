package lt.vu.mif.jate.task01.bank;

import lombok.Getter;
import lt.vu.mif.jate.task01.bank.exception.IBANException;
import lt.vu.mif.jate.task01.bank.exception.NoFundsException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract Account class.
 *
 * Not meant to be used directly, but via Banking facade.
 */
public abstract class Account {

    /**
     * IBAN object.
     */
    private IBAN iban;

    /**
     * Reference to a bank.
     */
    @Getter
    private Bank bank;

    /**
     * Map of currency to balance.
     */
    private Map<Currency, BigDecimal> balances = new HashMap<>();

    /**
     * Constructor, that takes IBAN as a string.
     * @param x IBAN string
     * @throws IBANException Malformed IBAN string
     */
    protected Account(final String x) throws IBANException {
        this(IBANParser.parse(x));
    }

    /**
     * Constructor, that takes the IBAN object.
     * @param x IBAN object
     */
    private Account(final IBAN x) {
        this.iban = x;
        this.bank = Banking.getInstance()
            .getBankOrCreate(iban.getCountry(), iban.getBankCode());
    }

    /**
     * Get account number.
     * @return Account number
     */
    public final BigInteger getNumber() {
        return iban.getAccountNumber();
    }

    /**
     * Credit an account.
     * @param amount Amount
     * @param currency Currency code
     */
    public final void credit(final BigDecimal amount, final String currency) {
        credit(amount, Currency.getInstance(currency));
    }

    /**
     * Credit an account.
     * @param amount Amount
     * @param currency Currency object
     */
    public abstract void credit(BigDecimal amount, Currency currency);

    /**
     * Debit an account.
     * @param amount Amount
     * @param currency Currency code
     */
    public final void debit(final BigDecimal amount, final String currency) {
        debit(amount, Currency.getInstance(currency));
    }

    /**
     * Debit an account.
     * @param amount Amount
     * @param currency Currency object
     */
    public abstract void debit(BigDecimal amount, Currency currency);

    /**
     * Transfer money to another account.
     * @param amount Amount
     * @param currency Currency code
     * @param creditAccount Target account
     */
    public final void debit(final BigDecimal amount, final String currency,
            final Account creditAccount) {
        debit(amount, Currency.getInstance(currency), creditAccount);
    }

    /**
     * Transfer money to another account.
     * @param amount Amount
     * @param currency Currency object
     * @param creditAccount Target account
     */
    public abstract void debit(BigDecimal amount, Currency currency,
            Account creditAccount);

    /**
     * Overrides the balance on account. Use with care.
     * @param amount Amount
     * @param currency Currency code
     */
    protected final void setBalance(final BigDecimal amount,
            final String currency) {
        setBalance(amount, Currency.getInstance(currency));
    }

    /**
     * Overrides the balance on account. Use with care.
     * @param amount Amount
     * @param currency Currency object
     */
    protected final void setBalance(final BigDecimal amount,
            final Currency currency) {
        if (balances.containsKey(currency)) {
            balances.remove(currency);
        }
        balances.put(currency, amount);
    }

    /**
     * Get account balance.
     * @param currency Currency code
     * @return Balance amount
     */
    public final BigDecimal balance(final String currency) {
        return balance(Currency.getInstance(currency));
    }

    /**
     * Get account balance.
     * @param currency Currency object
     * @return Balance amount
     */
    public final BigDecimal balance(final Currency currency) {
        return balances
            .getOrDefault(currency, BigDecimal.ZERO)
            .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Get account balance.
     * @param currency Currency code
     * @return Balance amount
     */
    public final BigDecimal balanceAll(final String currency) {
        return balanceAll(Currency.getInstance(currency));
    }

    /**
     * Get account balance.
     * @param currency Currency object
     * @return Balance amount
     */
    public final BigDecimal balanceAll(final Currency currency) {
        BigDecimal balance = BigDecimal.ZERO;
        Converter conv = Converter.getInstance();
        for (Map.Entry<Currency, BigDecimal> entry: balances.entrySet()) {
            BigDecimal converted = conv.convert(entry.getValue(),
                entry.getKey(), currency);
            balance = balance.add(converted);
        }
        return balance.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Convert currencies on this account.
     * @param amount Amount
     * @param currencyFrom From currency
     * @param currencyTo To currency
     */
    public final void convert(final BigDecimal amount,
            final String currencyFrom, final String currencyTo) {
        Converter conv = Converter.getInstance();
        BigDecimal balanceFrom = balance(currencyFrom);
        if (balanceFrom.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NoFundsException();
        }
        if (balanceFrom.compareTo(amount) < 0) {
            throw new NoFundsException();
        }
        BigDecimal balanceTo = balance(currencyTo);
        BigDecimal converted = conv.convert(amount,
                Currency.getInstance(currencyFrom),
                Currency.getInstance(currencyTo));
        setBalance(balanceFrom.subtract(amount), currencyFrom);
        setBalance(balanceTo.add(converted), currencyTo);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj instanceof Account) {
            Account account = (Account) obj;
            return account.toString().equals(this.toString());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return iban.hashCode();
    }

    @Override
    public final String toString() {
        return iban.toString();
    }

}
