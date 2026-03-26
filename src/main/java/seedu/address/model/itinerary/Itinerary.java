package seedu.address.model.itinerary;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.Id;

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
    private final Set<Id> clientIds;
    private final Set<Id> vendorIds;

    /**
     * Every field must be present and not null.
     */
    public Itinerary(ItineraryName itineraryName, Destination destination,
                     DateRange dateRange) {
        requireAllNonNull(itineraryName, destination, dateRange);
        this.itineraryName = itineraryName;
        this.destination = destination;
        this.dateRange = dateRange;
        this.clientIds = new HashSet<>();
        this.vendorIds = new HashSet<>();
    }

    /**
     * Alternative constructor for reading from storage file
     */
    public Itinerary(ItineraryName itineraryName, Destination destination,
                     DateRange dateRange, Set<Id> clientIds, Set<Id> vendorIds) {
        requireAllNonNull(itineraryName, destination, dateRange, clientIds, vendorIds);
        this.itineraryName = itineraryName;
        this.destination = destination;
        this.dateRange = dateRange;
        this.clientIds = new HashSet<>(clientIds);
        this.vendorIds = new HashSet<>(vendorIds);
    }

    public void setClients(Set<Id> clientIds) {
        requireAllNonNull(clientIds);
        this.clientIds.addAll(clientIds);
    }

    public void setVendors(Set<Id> vendorIds) {
        requireAllNonNull(vendorIds);
        this.vendorIds.addAll(vendorIds);
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

    public Set<Id> getClientIds() {
        return Collections.unmodifiableSet(clientIds);
    }

    public Set<Id> getVendorIds() {
        return Collections.unmodifiableSet(vendorIds);
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

    /**
     * Removes a person from the itinerary.
     *
     * @param id id of the removed person.
     */
    public void removePersonId(Id id) {
        clientIds.remove(id);
        vendorIds.remove(id);
    }

    /**
     * Checks whether the Itinerary contain specific person.
     *
     * @param id The id of specific person.
     */
    public boolean containsPerson(Id id) {
        return clientIds.contains(id) || vendorIds.contains(id);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Itinerary)) {
            return false;
        }

        Itinerary otherItinerary = (Itinerary) other;
        return itineraryName.equals(otherItinerary.itineraryName)
                && destination.equals(otherItinerary.destination)
                && dateRange.equals(otherItinerary.dateRange)
                && clientIds.equals(otherItinerary.clientIds)
                && vendorIds.equals(otherItinerary.vendorIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itineraryName, destination, dateRange, clientIds, vendorIds);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("itineraryName", itineraryName)
                .add("destination", destination)
                .add("date range", dateRange)
                .add("clientIds", clientIds)
                .add("vendorIds", vendorIds)
                .toString();
    }

}
