package seedu.address.testutil;

import seedu.address.model.itinerary.DateRange;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryName;

/**
 * A utility class to help with building Itinerary objects.
 */
public class ItineraryBuilder {

    public static final String DEFAULT_NAME = "5D4N Trip to France";
    public static final String DEFAULT_DESTINATION = "France";
    public static final String DEFAULT_START_DATE = "2024-12-01";
    public static final String DEFAULT_END_DATE = "2024-12-05";

    private ItineraryName itineraryName;
    private Destination destination;
    private DateRange dateRange;

    /**
     * Creates a {@code ItineraryBuilder} with the default details.
     */
    public ItineraryBuilder() {
        itineraryName = new ItineraryName(DEFAULT_NAME);
        destination = new Destination(DEFAULT_DESTINATION);
        dateRange = new DateRange(DEFAULT_START_DATE, DEFAULT_END_DATE);
    }

    /**
     * Initializes the ItineraryBuilder with the data of {@code itineraryToCopy}.
     */
    public ItineraryBuilder(Itinerary itineraryToCopy) {
        itineraryName = itineraryToCopy.getName();
        destination = itineraryToCopy.getDestination();
        dateRange = itineraryToCopy.getDateRange();
    }

    /**
     * Sets the {@code ItineraryName} of the {@code Itinerary} that we are building.
     */
    public ItineraryBuilder withName(String name) {
        this.itineraryName = new ItineraryName(name);
        return this;
    }

    /**
     * Sets the {@code Destination} of the {@code Itinerary} that we are building.
     */
    public ItineraryBuilder withDestination(String destination) {
        this.destination = new Destination(destination);
        return this;
    }

    /**
     * Sets the {@code DateRange} of the {@code Itinerary} that we are building.
     */
    public ItineraryBuilder withDateRange(String startDate, String endDate) {
        this.dateRange = new DateRange(startDate, endDate);
        return this;
    }

    public Itinerary build() {
        return new Itinerary(itineraryName, destination, dateRange);
    }
}
