package lt.vu.mif.jate.task01.bank.exception;

import lombok.Getter;

/**
 * Error while parsing the IBAN.
 */
public class IBANException extends Exception {

    /**
     * IBAN.
     */
    @Getter
    private String value;

    /**
     * Constructor.
     * @param iban IBAN
     * @param message Exception message
     */
    public IBANException(final String iban, final String message) {
        super(message);
        this.value = iban;
    }

}
