package lt.vu.mif.jate.task01.bank;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Scanner;

/**
 * Utility class.
 */
final class Util {

    /**
     * Placeholder.
     */
    private Util() { }

    /**
     * Reads a resource file into a list of strings.
     * @param path Path to resource
     * @return List of strings
     */
    static ArrayList<String> readResourceFile(final String path) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File("src/test/resources/" + path);
        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Reads a CSV file into a list of string arrays.
     * @param path Path to resource
     * @param sep CSV delimiter
     * @return List of strings
     */
    static ArrayList<String[]> readResourceFileCSV(final String path,
            final String sep) {
        ArrayList<String[]> result = new ArrayList<>();
        ArrayList<String> lines = readResourceFile(path);
        for (String line: lines) {
            result.add(line.split(sep));
        }
        return result;
    }

    /**
     * Validates money amount, ensuring it is a positive decimal with 2 digit
     * precision.
     * @param value Value to be validated
     */
    static void validateAmount(final String value) {
        if (!value.matches("\\d*\\.?\\d+")) {
            throw new NumberFormatException();
        }
        validateAmount(new BigDecimal(value));
    }

    /**
     * Validates money amount, ensuring it is a positive decimal with 2 digit
     * precision.
     * @param value Value to be validated
     */
    static void validateAmount(final BigDecimal value) {
        if (value.scale() > 2) {
            throw new NumberFormatException();
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new NumberFormatException();
        }
    }

    /**
     * Validates a currency code.
     * @param currency Currency code to be validated
     */
    static void validateCurrency(final String currency) {
        if (Currency.getInstance(currency) == null) {
            throw new IllegalArgumentException();
        }
    }

}
