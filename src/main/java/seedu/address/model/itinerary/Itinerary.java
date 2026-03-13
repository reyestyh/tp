package seedu.address.model.itinerary;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an Itinerary in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Itinerary {

    // Identity fields
    private final Name name;

    // Data fields
    private final Destination destination;
    private final DateRange dateRange;

    /**
     * Every field must be present and not null.
     */
    public Itinerary(Name name, Destination destination, DateRange dateRange) {
        requireAllNonNull(name, destination, dateRange);
        this.name = name;
        this.destination = destination;
        this.dateRange = dateRange;
    }

    public Name getName() {
        return name;
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
                .add("Name", name)
                .add("Destination", destination)
                .add("Date Range", dateRange)
                .toString();
    }

}
