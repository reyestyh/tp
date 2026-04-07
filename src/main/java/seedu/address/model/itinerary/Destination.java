package seedu.address.model.itinerary;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Itinerary's destination in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDestination(String)}
 */
public class Destination {

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid destination: Please provide a non-empty destination.";

    public static final String VALIDATION_REGEX = "[\\p{L}\\p{N}].*";

    public final String destination;

    /**
     * Constructs a {@code Destination}.
     *
     * @param destination A valid destination.
     */
    public Destination(String destination) {
        requireNonNull(destination);
        checkArgument(isValidDestination(destination), MESSAGE_CONSTRAINTS);
        this.destination = destination;
    }

    /**
     * Returns true if a given string is a valid destination.
     */
    public static boolean isValidDestination(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Destination)) {
            return false;
        }

        Destination otherDestination = (Destination) other;
        return destination.equals(otherDestination.destination);
    }

    @Override
    public int hashCode() {
        return destination.hashCode();
    }

    @Override
    public String toString() {
        return destination;
    }

}
