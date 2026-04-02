package seedu.address.model.itinerary;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents an Itinerary's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class ItineraryName {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should not be empty";

    public static final String VALIDATION_REGEX = "[\\p{L}\\p{N}].*";

    public final String fullName;

    /**
     * Constructs a {@code ItineraryName}.
     *
     * @param name A valid name.
     */
    public ItineraryName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ItineraryName)) {
            return false;
        }

        ItineraryName otherItineraryName = (ItineraryName) other;
        return fullName.toLowerCase(Locale.ROOT)
                .equals(otherItineraryName.fullName.toLowerCase(Locale.ROOT));
    }

    @Override
    public int hashCode() {
        return fullName.toLowerCase(Locale.ROOT).hashCode();
    }
}
