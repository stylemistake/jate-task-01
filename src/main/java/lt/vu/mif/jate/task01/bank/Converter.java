package lt.vu.mif.jate.task01.bank;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Currency converter singleton.
 */
public final class Converter {

    /**
     * A singleton instance.
     */
    private static Converter instance;

    /**
     * Get instance of currency converter service.
     * @return Instance
     */
    static synchronized Converter getInstance() {
        if (instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    /**
     * Base currency.
     */
    @Getter
    private Currency baseCurrency = Currency.getInstance("EUR");

    /**
     * Map, holding convertion rates from base currency.
     */
    private Map<Currency, BigDecimal> ratesFrom = new HashMap<>();

    /**
     * Map, holding convertion rates to base currency.
     */
    private Map<Currency, BigDecimal> ratesTo = new HashMap<>();

    /**
     * Constructor.
     *
     * Reads convertion rates from the resource file.
     */
    private Converter() {
        // Read banks from file
        ArrayList<String[]> lines = Util
            .readResourceFileCSV("banking/rates.txt", ":");
        for (String[] line: lines) {
            try {
                Currency currency = Currency.getInstance(line[0]);
                ratesFrom.put(currency, new BigDecimal(line[2]));
                ratesTo.put(currency, new BigDecimal(line[1]));
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
    }

    /**
     * Currency code validator.
     * @param currency Currency code
     */
    private void validateCurrency(final String currency) {
        Util.validateCurrency(currency);
        validateCurrency(Currency.getInstance(currency));
    }

    /**
     * Currency object validator.
     * @param currency Currency object
     */
    private void validateCurrency(final Currency currency) {
        if (!ratesTo.containsKey(currency)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get convertion rate from base currency to chosen currency.
     * @param currency Currency code
     * @return Conversion rate
     */
    public BigDecimal getRateFromBase(final String currency) {
        return getRateFromBase(Currency.getInstance(currency));
    }

    /**
     * Get convertion rate from base currency to chosen currency.
     * @param currency Currency object
     * @return Conversion rate
     */
    public BigDecimal getRateFromBase(final Currency currency) {
        if (!ratesFrom.containsKey(currency)) {
            throw new IllegalArgumentException();
        }
        return ratesFrom.get(currency);
    }

    /**
     * Get convertion rate from chosen currency to chosen currency.
     * @param currency Currency code
     * @return Conversion rate
     */
    public BigDecimal getRateToBase(final String currency) {
        return getRateToBase(Currency.getInstance(currency));
    }

    /**
     * Get convertion rate from chosen currency to chosen currency.
     * @param currency Currency object
     * @return Conversion rate
     */
    public BigDecimal getRateToBase(final Currency currency) {
        if (!ratesTo.containsKey(currency)) {
            throw new IllegalArgumentException();
        }
        return ratesTo.get(currency);
    }

    /**
     * Convert amount from base currency to chosen currency.
     * @param value Amount
     * @param currency Currency code
     * @return Converted amount
     */
    public BigDecimal fromBase(final String value, final String currency) {
        Util.validateAmount(value);
        validateCurrency(currency);
        return new BigDecimal(value)
                .multiply(getRateFromBase(currency))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Convert amount from chosen currency to base currency.
     * @param value Amount
     * @param currency Currency code
     * @return Converted amount
     */
    public BigDecimal toBase(final String value, final String currency) {
        Util.validateAmount(value);
        validateCurrency(currency);
        return new BigDecimal(value)
                .multiply(getRateToBase(currency))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Convert amount from any currency to any other currency.
     * @param value Amount
     * @param currencyFrom Currency code to convert from
     * @param currencyTo Currency code to convert to
     * @return Converted amount
     */
    public BigDecimal convert(final String value,
            final String currencyFrom, final String currencyTo) {
        Util.validateAmount(value);
        validateCurrency(currencyFrom);
        validateCurrency(currencyTo);
        if (currencyFrom.equals(currencyTo)) {
            return new BigDecimal(value);
        }
        return new BigDecimal(value)
                .multiply(getRateToBase(currencyFrom))
                .multiply(getRateFromBase(currencyTo))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Convert amount from any currency to any other currency.
     * @param value Amount
     * @param currencyFrom Currency object to convert from
     * @param currencyTo Currency object to convert to
     * @return Converted amount
     */
    public BigDecimal convert(final BigDecimal value,
            final Currency currencyFrom, final Currency currencyTo) {
        Util.validateAmount(value);
        validateCurrency(currencyFrom);
        validateCurrency(currencyTo);
        if (currencyFrom.equals(currencyTo)) {
            return value;
        }
        return value
                .multiply(getRateToBase(currencyFrom))
                .multiply(getRateFromBase(currencyTo))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Get a Set with all available currencies.
     * @return Currency Set
     */
    public Set<Currency> getCurrencies() {
        return ratesFrom.keySet();
    }

}
