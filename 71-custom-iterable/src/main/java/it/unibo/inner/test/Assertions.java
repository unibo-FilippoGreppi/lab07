package it.unibo.inner.test;

import java.util.Collection;
import java.util.Objects;

/**
 * Utilities for testing.
 */
public final class Assertions {

    private static final String ERROR_HEADER = "# ERROR! ";
    private static final String ERROR_FOOTER = " #";
    private static final String EXPECTED_HEADER = "- As expected: ";

    private Assertions() { }

    /**
     * Exits with an error if the condition is not met.
     *
     * @param condition the condition to check
     */
    public static void assertTrue(final boolean condition) {
        if (condition) {
            confirmOK(true, condition);
        } else {
            printAndExit(1, "expected true, but got false");
        }
    }

    /**
     * Exits with an error if the two collections do not contain the same elements (except for the order).
     *
     * @param expected the expected collection
     * @param actual the actual collection
     */
    public static void assertContentEqualsInOrder(final Iterable<?> expected, final Collection<?> actual) {
        if (checkContentEqualsInOrder(expected, actual)) {
            confirmOK(expected, actual);
        } else {
            onNotEquals(expected, actual);
        }
    }

    private static long iterableSize(Iterable<?> iter) {
        var counter = 0L;
        for (var elem : iter) {
            counter++;
        }
        return counter;
    }

    private static boolean checkContentEqualsInOrder(final Iterable<?> expected, final Collection<?> actual) {
        Objects.requireNonNull(expected);
        if (actual == null || iterableSize(expected) != actual.size()) {
            return false;
        }
        final var expectedIterator = expected.iterator();
        final var actualIterator = actual.iterator();
        while (expectedIterator.hasNext()) {
            final var actualElement = actualIterator.next();
            final var expectedElement = expectedIterator.next();
            if (!expectedElement.equals(actualElement)) {
                return false;
            }
        }
        return true;
    }


    private static void confirmOK(final Object expected, final Object actual) {
        System.out.println(EXPECTED_HEADER + expected + " expected, and " + actual + " received."); // NOPMD
    }

    private static void onNotEquals(final Object expected, final Object actual) {
        printAndExit(2, "ERROR: expected " + expected + ", but got " + actual);
    }

    private static void printAndExit(final int errorCode, final String message) {
        final var error = ERROR_HEADER + message + ERROR_FOOTER;
        final var decoration = "#".repeat(error.length());
        final var builder = new StringBuilder(3 * error.length() + 2)
            .append(decoration)
            .append('\n')
            .append(error)
            .append('\n')
            .append(decoration);
        System.err.println(builder); // NOPMD
        System.exit(errorCode);
    }
}
