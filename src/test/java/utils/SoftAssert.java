package utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

public class SoftAssert {

    private static List<String> failures = new ArrayList<>();

    public static void assertEquals(Object expected, Object actual, String message) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (AssertionError e) {
            failures.add(message + " Expected: <" + expected + "> but was: <" + actual + ">");
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        try {
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
            Assert.assertEquals(expected, actual);
        } catch (AssertionError e) {
            failures.add("SoftAssertions Expected: <" + expected + "> but was: <" + actual + ">");
        }
    }

    public static void assertTrue(Object expected) {
        try {
            Assert.assertEquals(expected, true);
        } catch (AssertionError e) {
            failures.add("SoftAssertTrue Expected: <" + expected + ">");
        }
    }

    public static void assertTrue(boolean condition, String message) {
        try {
            Assert.assertTrue(condition);
        } catch (AssertionError e) {
            failures.add("SoftAssertTrue failed " + message);
        }
    }

    // Add other soft assertion methods as needed (assertTrue, assertFalse, etc.)

    public static void assertAll() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>ASSERT ALL IS EXECUTED<<<<<<<<<<<<<<<<<<<<<<");

        if (!failures.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Soft Assertion Failures:\n");

            for (String failure : failures) {
                errorMessage.append("- ").append(failure).append("\n");
            }

            failures.clear(); // Clear failures for the next test
            Assert.fail(errorMessage.toString());
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>ASSERT ALL IS EXECUTED<<<<<<<<<<<<<<<<<<<<<<");
    }
}