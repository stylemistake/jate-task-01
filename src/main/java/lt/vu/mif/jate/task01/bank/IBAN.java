package lt.vu.mif.jate.task01.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

/**
 * IBAN.
 */
@AllArgsConstructor
public class IBAN {

    /**
     * IBAN number.
     */
    @Getter
    private String iban;

    /**
     * Country code.
     */
    @Getter
    private String country;

    /**
     * Bank code.
     */
    @Getter
    private int bankCode;

    /**
     * Account number.
     */
    @Getter
    private BigInteger accountNumber;

    @Override
    public final String toString() {
        return iban;
    }

}
