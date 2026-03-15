package seedu.address.model.itinerary;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an Itinerary in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Itinerary {


    // Identity fields
    private final ItineraryName itineraryName;

    // Data fields
    private final Destination destination;
    private final DateRange dateRange;

    /**
     * Every field must be present and not null.
     */
    public Itinerary(ItineraryName itineraryName, Destination destination, DateRange dateRange) {
        requireAllNonNull(itineraryName, destination, dateRange);
        this.itineraryName = itineraryName;
        this.destination = destination;
        this.dateRange = dateRange;
    }

    public ItineraryName getName() {
        return itineraryName;
    }

    public Destination getDestination() {
        return destination;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    /**
     * Returns true if both itineraries have the same name.
     */
    public boolean isSameItinerary(Itinerary otherItinerary) {
        if (otherItinerary == this) {
            return true;
        }

        return otherItinerary != null
                && otherItinerary.getName().equals(getName());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("itineraryName", itineraryName)
                .add("destination", destination)
                .add("date range", dateRange)
                .toString();
    }

}
