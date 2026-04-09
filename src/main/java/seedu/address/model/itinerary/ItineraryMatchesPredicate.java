package seedu.address.model.itinerary;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether an {@code Itinerary} is the same as any of the given itineraries.
 */
public class ItineraryMatchesPredicate implements Predicate<Itinerary> {
    private final List<Itinerary> itineraries;

    public ItineraryMatchesPredicate(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    @Override
    public boolean test(Itinerary itinerary) {
        return itineraries.stream()
                .anyMatch(i -> i.isSameItinerary(itinerary));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ItineraryMatchesPredicate)) {
            return false;
        }

        ItineraryMatchesPredicate otherItineraryMatchesPredicate = (ItineraryMatchesPredicate) other;
        return itineraries.equals(otherItineraryMatchesPredicate.itineraries);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("itineraries", itineraries).toString();
    }
}
