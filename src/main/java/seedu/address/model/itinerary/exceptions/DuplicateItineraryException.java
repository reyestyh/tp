package seedu.address.model.itinerary.exceptions;

/**
 * Signals that the operation will result in duplicate Itineraries (Itineraries are considered duplicates if they have
 * the same name).
 */
public class DuplicateItineraryException extends RuntimeException {
    public DuplicateItineraryException() {
        super("Operation would result in duplicate itineraries");
    }
}
