package lt.vu.mif.jate.task01.bank.exception;

/**
 * Wrong account type exception.
 */
public class WrongAccountTypeException extends Exception {

    /**
     * Constructor.
     * @param message Message
     */
    public WrongAccountTypeException(final String message) {
        super(message);
    }

}
