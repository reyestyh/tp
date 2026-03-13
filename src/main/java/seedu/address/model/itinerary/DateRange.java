package seedu.address.model.itinerary;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an Itinerary's date range in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class DateRange {

    public static final String MESSAGE_CONSTRAINTS = "NOT IMPLEMENTED";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final String VALIDATION_REGEX = "";

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
    public boolean isValidDateRange(String startDate, String endDate) {
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

    @Override
    public String toString() {
        return startDate.format(DATE_FORMAT) + " - " + endDate.format(DATE_FORMAT);
    }
}
