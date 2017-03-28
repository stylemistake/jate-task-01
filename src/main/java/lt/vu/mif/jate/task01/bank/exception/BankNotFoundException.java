package lt.vu.mif.jate.task01.bank.exception;

import lombok.Getter;

public class BankNotFoundException extends Exception {

    @Getter
    private String country;

    @Getter
    private Integer code;

    public BankNotFoundException(final String country, final Integer code) {
        super("Bank (" + country + "-" + code + ") was not found.");
        this.country = country;
        this.code = code;
    }

}
