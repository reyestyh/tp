package seedu.address.model.itinerary;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Itinerary}'s {@code ItineraryName} matches any of the names given.
 */
public class ItineraryNameMatchesPredicate implements Predicate<Itinerary> {
    private final List<ItineraryName> names;

    public ItineraryNameMatchesPredicate(List<ItineraryName> names) {
        this.names = names;
    }

    @Override
    public boolean test(Itinerary itinerary) {
        return names.stream()
                .anyMatch(name -> name.equals(itinerary.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ItineraryNameMatchesPredicate)) {
            return false;
        }

        ItineraryNameMatchesPredicate otherItineraryNameMatchesPredicate = (ItineraryNameMatchesPredicate) other;
        return names.equals(otherItineraryNameMatchesPredicate.names);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("names", names).toString();
    }
}
