package seedu.address.model.itinerary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DateRangeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DateRange(null, null));
    }

    @Test
    public void constructor_invalidDateRange_throwsIllegalArgumentException() {
        // end date before start date
        assertThrows(IllegalArgumentException.class,
                DateRange.MESSAGE_INVALID_DATE_RANGE, () -> new DateRange("2026-01-01", "2025-12-31"));

        // invalid end date
        assertThrows(IllegalArgumentException.class,
                DateRange.MESSAGE_INVALID_DATE, () -> new DateRange("2026-01-01", "2026-01-32"));

        // invalid start date
        assertThrows(IllegalArgumentException.class,
                DateRange.MESSAGE_INVALID_DATE, () -> new DateRange("2026-02-32", "2026-03-01"));

        // invalid format
        assertThrows(IllegalArgumentException.class,
                DateRange.MESSAGE_INVALID_DATE_FORMAT, () -> new DateRange("01 Jan 2026", "01/02/2026"));
    }

    @Test
    public void isValidDateRange() {
        // null date range
        assertThrows(NullPointerException.class, () -> DateRange.isValidDateRange(null, null));

        // invalid date ranges
        assertFalse(DateRange.isValidDateRange("2026-01-01", "2025-12-31")); // end date before start date
        assertFalse(DateRange.isValidDateRange("2026-01-01", "2026-01-32")); // invalid end date
        assertFalse(DateRange.isValidDateRange("2026-02-32", "2026-03-01")); // invalid start date

        // valid date ranges
        assertTrue(DateRange.isValidDateRange("2026-01-01", "2026-01-05")); // start date before end date
        assertTrue(DateRange.isValidDateRange("2026-01-01", "2026-01-01")); // start date same as end date
    }

    @Test
    public void isValidDateFormat() {
        // invalid date formats
        assertFalse(DateRange.isValidDateFormat("01 Jan 2026")); // alphanumeric date
        assertFalse(DateRange.isValidDateFormat("2026/01/01")); // wrong separator
        assertFalse(DateRange.isValidDateFormat("2026-01-010")); // extra character
        assertFalse(DateRange.isValidDateFormat("2026-01-1")); // missing character

        // valid date formats
        assertTrue(DateRange.isValidDateFormat("2026-01-01")); // normal date
        assertTrue(DateRange.isValidDateFormat("2026-02-30")); // non-existent date, but correct date format
    }

    @Test
    public void equals() {
        DateRange dateRange = new DateRange("2026-01-01", "2026-01-05");

        // same values -> returns true
        assertTrue(dateRange.equals(new DateRange("2026-01-01", "2026-01-05")));

        // same object -> returns true
        assertTrue(dateRange.equals(dateRange));

        // null -> returns false
        assertFalse(dateRange.equals(null));

        // different types -> returns false
        assertFalse(dateRange.equals(5.0f));

        // different values -> returns false
        assertFalse(dateRange.equals(new DateRange("2026-02-01", "2026-02-05")));
    }
}
