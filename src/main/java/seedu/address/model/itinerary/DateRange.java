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

    public static final String MESSAGE_CONSTRAINTS = "Dates should be in the format YYYY-MM-DD, "
            + "and the end date should not be before the start date";

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
        checkArgument(isValidDateRange(startDate, endDate), MESSAGE_CONSTRAINTS);
        this.startDate = LocalDate.parse(startDate, DATE_FORMAT);
        this.endDate = LocalDate.parse(endDate, DATE_FORMAT);
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
