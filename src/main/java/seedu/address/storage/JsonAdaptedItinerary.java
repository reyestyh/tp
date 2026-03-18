package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.itinerary.DateRange;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryName;

/**
 * Jackson-friendly version of {@link Itinerary}.
 */
public class JsonAdaptedItinerary {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Itinerary's %s field is missing!";
    public static final String MISSING_START_DATE_MESSAGE = "Itinerary's start date is missing!";
    public static final String MISSING_END_DATE_MESSAGE = "Itinerary's end date is missing!";

    private final String name;
    private final String destination;
    private final String startDate;
    private final String endDate;
    private final List<String> clientIds = new ArrayList<>();
    private final List<String> vendorIds = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedItinerary} with the given itinerary details.
     */
    @JsonCreator
    public JsonAdaptedItinerary(@JsonProperty("name") String name,
            @JsonProperty("destination") String destination, @JsonProperty("start date") String startDate,
            @JsonProperty("end date") String endDate, @JsonProperty("client ids") List<String> clientIds,
            @JsonProperty("vendor ids") List<String> vendorIds) {
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        if (clientIds != null) {
            this.clientIds.addAll(clientIds);
        }
        if (vendorIds != null) {
            this.vendorIds.addAll(vendorIds);
        }
    }

    /**
     * Converts a given {@code Itinerary} into this class for Jackson use.
     */
    public JsonAdaptedItinerary(Itinerary source) {
        name = source.getName().fullName;
        destination = source.getDestination().destination;
        startDate = source.getDateRange().startDate.toString();
        endDate = source.getDateRange().endDate.toString();
        clientIds.addAll(source.getClientIds().stream().map(UUID::toString).collect(Collectors.toList()));
        vendorIds.addAll(source.getVendorIds().stream().map(UUID::toString).collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted itinerary object into the model's {@code Itinerary} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted itinerary.
     */
    public Itinerary toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ItineraryName.class.getSimpleName()));
        }
        if (!ItineraryName.isValidName(name)) {
            throw new IllegalValueException(ItineraryName.MESSAGE_CONSTRAINTS);
        }
        final ItineraryName modelName = new ItineraryName(name);

        if (destination == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Destination.class.getSimpleName()));
        }
        if (!Destination.isValidDestination(destination)) {
            throw new IllegalValueException(Destination.MESSAGE_CONSTRAINTS);
        }
        final Destination modelDestination = new Destination(destination);

        if (startDate == null) {
            throw new IllegalValueException(MISSING_START_DATE_MESSAGE);
        }
        if (endDate == null) {
            throw new IllegalValueException(MISSING_END_DATE_MESSAGE);
        }
        if (!DateRange.isValidDateRange(startDate, endDate)) {
            throw new IllegalValueException((DateRange.MESSAGE_CONSTRAINTS));
        }
        final DateRange dateRange = new DateRange(startDate, endDate);

        Set<UUID> modelClientIds = clientIds.stream().map(UUID::fromString).collect(Collectors.toSet());
        Set<UUID> modelVendorIds = vendorIds.stream().map(UUID::fromString).collect(Collectors.toSet());
        return new Itinerary(modelName, modelDestination, dateRange, modelClientIds, modelVendorIds);
    }

}
