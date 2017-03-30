package lt.vu.mif.jate.task01.bank;

import lombok.Getter;
import lombok.NonNull;

import java.util.Locale;

/**
 * Bank class.
 */
public class Bank {

    /**
     * Bank Locale object, that holds information about the country.
     */
    @Getter
    private Locale locale;

    /**
     * Bank code.
     */
    @Getter
    private Integer code;

    /**
     * Bank BIC code.
     */
    @Getter
    private String bicCode;

    /**
     * Bank name.
     */
    @Getter
    private String name;

    /**
     * Bank address.
     */
    @Getter
    private String address;

    /**
     * Shorthand bank constructor.
     * @param a Bank country code
     * @param b Bank code
     */
    public Bank(final String a, final Integer b) {
        this(a, b, null, null, null);
    }

    /**
     * Shorthand bank constructor.
     * @param a Bank country code
     * @param b Bank code
     * @param c Bank BIC code
     */
    public Bank(final String a, final Integer b, final String c) {
        this(a, b, c, null, null);
    }

    /**
     * Shorthand bank constructor.
     * @param a Bank country code
     * @param b Bank code
     * @param c Bank BIC code
     * @param d Bank name
     */
    public Bank(final String a, final Integer b, final String c,
            final String d) {
        this(a, b, c, d, null);
    }

    /**
     * Bank constructor.
     * @param a Bank country code
     * @param b Bank code
     * @param c Bank BIC code
     * @param d Bank name
     * @param e Bank address
     */
    public Bank(@NonNull final String a, @NonNull final Integer b,
            final String c, final String d, final String e) {
        this.locale = new Locale(a, a);
        this.code = b;
        this.bicCode = c;
        this.name = d;
        this.address = e;
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
