package ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Helper class for everything related to the console: printing banners,
 * headings, messages and tables, and reading validated user input.
 */
public class ConsoleUI {

    private static final int WIDTH = 60;
    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleUI() {
        // utility class - no objects needed
    }

    // ------------------------------------------------------------------
    // Output
    // ------------------------------------------------------------------

    public static void printBanner() {
        String line = "=".repeat(WIDTH);
        System.out.println(line);
        System.out.println(center("COLLEGE EVENT REGISTRATION PORTAL"));
        System.out.println(center("Explore  |  Register  |  Participate"));
        System.out.println(line);
    }

    public static void printHeading(String title) {
        System.out.println();
        System.out.println("-".repeat(WIDTH));
        System.out.println(center(title.toUpperCase()));
        System.out.println("-".repeat(WIDTH));
    }

    public static void success(String message) {
        System.out.println("  [OK] " + message);
    }

    public static void error(String message) {
        System.out.println("  [X]  " + message);
    }

    public static void info(String message) {
        System.out.println("  [i]  " + message);
    }

    /**
     * Prints rows as a bordered table. Column widths are calculated from the
     * longest value in each column.
     *
     * @param headers column titles
     * @param rows    table data, one {@code String[]} per row
     */
    public static void printTable(String[] headers, ArrayList<String[]> rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (row[i].length() > widths[i]) {
                    widths[i] = row[i].length();
                }
            }
        }

        String border = buildBorder(widths);
        System.out.println(border);
        System.out.println(buildRow(headers, widths));
        System.out.println(border);
        for (String[] row : rows) {
            System.out.println(buildRow(row, widths));
        }
        System.out.println(border);
    }

    private static String buildBorder(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int width : widths) {
            sb.append("-".repeat(width + 2)).append("+");
        }
        return sb.toString();
    }

    private static String buildRow(String[] cells, int[] widths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < cells.length; i++) {
            sb.append(" ").append(String.format("%-" + widths[i] + "s", cells[i])).append(" |");
        }
        return sb.toString();
    }

    private static String center(String text) {
        int padding = (WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    // ------------------------------------------------------------------
    // Input
    // ------------------------------------------------------------------

    /** Reads a non-empty line of text. */
    public static String readText(String prompt) {
        while (true) {
            System.out.print("  > " + prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            error("This field cannot be empty.");
        }
    }

    /** Reads a whole number, asking again until the input is valid. */
    public static int readInt(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                error("Please enter a valid number.");
            }
        }
    }

    /** Reads a date in YYYY-MM-DD format, asking again until it is valid. */
    public static LocalDate readDate(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                error("Invalid date. Use the format YYYY-MM-DD.");
            }
        }
    }
}
