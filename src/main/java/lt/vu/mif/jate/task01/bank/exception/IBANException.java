package lt.vu.mif.jate.task01.bank.exception;

import lombok.Getter;

public class IBANException extends Exception {

    @Getter
    private String value;

    public IBANException(final String iban, final String message) {
        super(message);
        this.value = iban;
    }

}
