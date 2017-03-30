package lt.vu.mif.jate.task01.bank.exception;

import lombok.Getter;

/**
 * Bank not found.
 */
public class BankNotFoundException extends Exception {

    /**
     * Bank country.
     */
    @Getter
    private String country;

    /**
     * Bank code.
     */
    @Getter
    private Integer code;

    /**
     * Constructor.
     * @param a Bank country
     * @param b Bank code
     */
    public BankNotFoundException(final String a, final Integer b) {
        super("Bank (" + a + "-" + b + ") was not found.");
        this.country = a;
        this.code = b;
    }

}
