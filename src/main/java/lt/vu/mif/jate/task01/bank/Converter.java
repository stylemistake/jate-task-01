package lt.vu.mif.jate.task01.bank;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Converter {

    private static Converter instance;

    static synchronized Converter getInstance() {
        if (instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    @Getter
    private Currency baseCurrency = Currency.getInstance("EUR");

    private Map<Currency, BigDecimal> ratesFrom = new HashMap<>();
    private Map<Currency, BigDecimal> ratesTo = new HashMap<>();

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

    private void validateCurrency(final String currency) {
        Util.validateCurrency(currency);
        validateCurrency(Currency.getInstance(currency));
    }

    private void validateCurrency(final Currency currency) {
        if (!ratesTo.containsKey(currency)) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getRateFromBase(final String currency) {
        return getRateFromBase(Currency.getInstance(currency));
    }

    public BigDecimal getRateFromBase(final Currency currency) {
        if (!ratesFrom.containsKey(currency)) {
            throw new IllegalArgumentException();
        }
        return ratesFrom.get(currency);
    }

    public BigDecimal getRateToBase(final String currency) {
        return getRateToBase(Currency.getInstance(currency));
    }

    public BigDecimal getRateToBase(final Currency currency) {
        if (!ratesTo.containsKey(currency)) {
            throw new IllegalArgumentException();
        }
        return ratesTo.get(currency);
    }

    public BigDecimal fromBase(final String value, final String currency) {
        Util.validateAmount(value);
        validateCurrency(currency);
        return new BigDecimal(value)
                .multiply(getRateFromBase(currency))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal toBase(final String value, final String currency) {
        Util.validateAmount(value);
        validateCurrency(currency);
        return new BigDecimal(value)
                .multiply(getRateToBase(currency))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal convert(final String value, final String currencyFrom,
                              final String currencyTo) {
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

    public BigDecimal convert(final BigDecimal value,
                              final Currency currencyFrom,
                              final Currency currencyTo) {
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

    public Set<Currency> getCurrencies() {
        return ratesFrom.keySet();
    }

}
