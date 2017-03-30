package lt.vu.mif.jate.task01.bank;

import lt.vu.mif.jate.task01.bank.exception.AccountActionException;
import lt.vu.mif.jate.task01.bank.exception.IBANException;
import lt.vu.mif.jate.task01.bank.exception.NoFundsException;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Credit account
 *
 * CreditAccount can be credited only once after opening!
 */
public class CreditAccount extends Account {

    /**
     * Flag, which restricts usage of "credit" function to only one time.
     */
    private boolean creditedOnce = false;

    /**
     * Constructor.
     * @param iban IBAN string
     * @throws IBANException IBAN parsing error
     */
    CreditAccount(final String iban) throws IBANException {
        super(iban);
    }

    @Override
    public final void credit(final BigDecimal amount, final Currency currency) {
        if (creditedOnce) {
            throw new AccountActionException();
        }
        creditedOnce = true;
        Util.validateAmount(amount);
        setBalance(balance(currency).add(amount), currency);
    }

    @Override
    public final void debit(final BigDecimal amount, final Currency currency) {
        Util.validateAmount(amount);
        BigDecimal balance = balance(currency);
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NoFundsException();
        }
        setBalance(balance(currency).subtract(amount), currency);
    }

    @Override
    public final void debit(final BigDecimal amount, final Currency currency,
                            final Account creditAccount) {
        Util.validateAmount(amount);
        debit(amount, currency);
        creditAccount.credit(amount, currency);
    }

}
