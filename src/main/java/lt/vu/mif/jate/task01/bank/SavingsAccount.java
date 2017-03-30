package lt.vu.mif.jate.task01.bank;

import lt.vu.mif.jate.task01.bank.exception.AccountActionException;
import lt.vu.mif.jate.task01.bank.exception.IBANException;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Savings account.
 */
public class SavingsAccount extends Account {

    /**
     * Constructor.
     * @param iban IBAN string
     * @throws IBANException IBAN parsing error
     */
    SavingsAccount(final String iban) throws IBANException {
        super(iban);
    }

    @Override
    public final void credit(final BigDecimal amount, final Currency currency) {
        Util.validateAmount(amount);
        setBalance(balance(currency).add(amount), currency);
    }

    @Override
    public final void debit(final BigDecimal amount, final Currency currency) {
        Util.validateAmount(amount);
        throw new AccountActionException();
    }

    @Override
    public final void debit(final BigDecimal amount, final Currency currency,
                            final Account creditAccount) {
        Util.validateAmount(amount);
        debit(amount, currency);
        creditAccount.credit(amount, currency);
    }

}
