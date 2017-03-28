package lt.vu.mif.jate.task01.bank;

import lombok.Getter;
import lombok.NonNull;

import java.util.Locale;

public class Bank {

    @Getter
    private Locale locale;

    @Getter
    private Integer code;

    @Getter
    private String bicCode;

    @Getter
    private String name;

    @Getter
    private String address;

    public Bank(final String country, final Integer code) {
        this(country, code, null, null, null);
    }

    public Bank(final String country, final Integer code,
                final String bicCode) {
        this(country, code, bicCode, null, null);
    }

    public Bank(final String country, final Integer code,
                final String bicCode, final String name) {
        this(country, code, bicCode, name, null);
    }

    public Bank(@NonNull final String country, @NonNull final Integer code,
                final String bicCode, final String name,
                final String address) {
        this.locale = new Locale(country, country);
        this.code = code;
        this.bicCode = bicCode;
        this.name = name;
        this.address = address;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj instanceof Bank) {
            Bank bank = (Bank) obj;
            return bank.getCode().equals(code)
                && bank.getLocale().equals(locale);
        }
        return false;
    }

    /**
     * A magic number, that determines the size of Locale in bits.
     */
    private static final int LOCALE_MAX_BITS = 17;

    @Override
    public final int hashCode() {
        return getLocale().hashCode() << LOCALE_MAX_BITS | code;
    }

    @Override
    public final String toString() {
        if (name != null) {
            return name;
        }
        if (bicCode != null) {
            return "Bank#" + code + " (" + bicCode + "), "
                + locale.getDisplayCountry();
        }
        return "Bank#" + code + ", " + locale.getDisplayCountry();
    }

}
