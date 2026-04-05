package seedu.address.model.itinerary;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents an Itinerary's date range in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class DateRange {
    public static final String MESSAGE_CONSTRAINTS = "Invalid date: Please use the YYYY-MM-DD format "
            + "and ensure the start date is before the end date.";
    public static final String MESSAGE_INVALID_DATE_FORMAT = "Invalid date format: Please use the YYYY-MM-DD format. \n"
            + "Example: 2026-10-12";
    public static final String MESSAGE_INVALID_DATE = "Invalid date: The date does not exist. \n"
            + "Example: 2026-02-30 or 2026-13-01 are invalid";
    public static final String MESSAGE_INVALID_DATE_RANGE =
            "Invalid date range: Start date must be before the end date.";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public final LocalDate startDate;
    public final LocalDate endDate;

    /**
     * Constructs a {@code DateRange}.
     *
     * @param startDate A valid start date.
     * @param endDate A valid end date.
     */
    public DateRange(String startDate, String endDate) {
        requireNonNull(startDate, endDate);

        checkArgument(isValidDateFormat(startDate), MESSAGE_INVALID_DATE_FORMAT);
        checkArgument(isValidDateFormat(endDate), MESSAGE_INVALID_DATE_FORMAT);

        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startDate, DATE_FORMAT);
            end = LocalDate.parse(endDate, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_INVALID_DATE);
        }

        checkArgument((end.isAfter(start) || end.isEqual(start)), MESSAGE_INVALID_DATE_RANGE);

        this.startDate = LocalDate.parse(startDate, DATE_FORMAT);
        this.endDate = LocalDate.parse(endDate, DATE_FORMAT);
    }

    /**
     * Returns true if a given string matches the YYYY-MM-DD format.
     */
    private static boolean isValidDateFormat(String date) {
        if (date.length() != 10) {
            return false;
        }
        if (date.charAt(4) != '-' || date.charAt(7) != '-') {
            return false;
        }
        for (int i = 0; i < date.length(); i += 1) {
            if (i != 4 && i != 7 && !Character.isDigit(date.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if a given string is a valid date range.
     */
    public static boolean isValidDateRange(String startDate, String endDate) {
        try {
            // Parse the strings. This automatically throws an exception
            // if the format is wrong OR if the date is impossible (like Feb 31).
            LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);
            return end.isAfter(start) || end.isEqual(start);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns the start date.
     */
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**
     * Returns the end date.
     */
    public LocalDate getEndDate() {
        return this.endDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DateRange)) {
            return false;
        }

        DateRange otherDateRange = (DateRange) other;
        return startDate.equals(otherDateRange.startDate)
                && endDate.equals(otherDateRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        return startDate.format(DATE_FORMAT) + " - " + endDate.format(DATE_FORMAT);
    }
}
