package lt.vu.mif.jate.task01.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class IBAN {

    @Getter
    private String iban;

    @Getter
    private String country;

    @Getter
    private int bankCode;

    @Getter
    private BigInteger accountNumber;

    @Override
    public final String toString() {
        return iban;
    }

}
