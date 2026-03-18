package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedItinerary.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalItineraries.FRANCE_TRIP;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.itinerary.DateRange;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.ItineraryName;

public class JsonAdaptedItineraryTest {
    private static final String INVALID_ITINERARY_NAME = "   ";
    private static final String INVALID_DESTINATION_NAME = "   ";
    private static final String INVALID_DATE = "2026-02-32";

    private static final String VALID_ITINERARY_NAME = FRANCE_TRIP.getName().toString();
    private static final String VALID_DESTINATION = FRANCE_TRIP.getDestination().toString();
    private static final String VALID_START_DATE = FRANCE_TRIP.getDateRange().startDate.toString();
    private static final String VALID_END_DATE = FRANCE_TRIP.getDateRange().endDate.toString();
    private static final List<String> VALID_CLIENT_IDS = FRANCE_TRIP.getClientIds().stream().map(UUID::toString)
            .collect(Collectors.toList());
    private static final List<String> VALID_VENDOR_IDS = FRANCE_TRIP.getVendorIds().stream().map(UUID::toString)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validItineraryDetails_returnsItinerary() throws Exception {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(FRANCE_TRIP);
        assertEquals(FRANCE_TRIP, itinerary.toModelType());
    }

    @Test
    public void toModelType_invalidItineraryName_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(INVALID_ITINERARY_NAME,
                VALID_DESTINATION, VALID_START_DATE, VALID_END_DATE, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = ItineraryName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }

    @Test
    public void toModelType_nullItineraryName_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(null, VALID_DESTINATION, VALID_START_DATE,
                VALID_END_DATE, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, ItineraryName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }

    @Test
    public void toModelType_invalidDestination_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(VALID_ITINERARY_NAME, INVALID_DESTINATION_NAME,
                VALID_START_DATE, VALID_END_DATE, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = Destination.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }

    @Test
    public void toModelType_nullDestination_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(VALID_ITINERARY_NAME, null,
                VALID_START_DATE, VALID_END_DATE, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Destination.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }

    @Test
    public void toModelType_invalidStartDate_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(VALID_ITINERARY_NAME, VALID_DESTINATION,
                INVALID_DATE, VALID_END_DATE, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = DateRange.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }

    @Test
    public void toModelType_nullStartDate_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(VALID_ITINERARY_NAME, VALID_DESTINATION,
                null, VALID_END_DATE, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = JsonAdaptedItinerary.MISSING_START_DATE_MESSAGE;
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }

    @Test
    public void toModelType_invalidEndDate_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(VALID_ITINERARY_NAME, VALID_DESTINATION,
                VALID_START_DATE, INVALID_DATE, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = DateRange.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }

    @Test
    public void toModelType_nullEndDate_throwsIllegalValueException() {
        JsonAdaptedItinerary itinerary = new JsonAdaptedItinerary(VALID_ITINERARY_NAME, VALID_DESTINATION,
                VALID_START_DATE, null, VALID_CLIENT_IDS, VALID_VENDOR_IDS);
        String expectedMessage = JsonAdaptedItinerary.MISSING_END_DATE_MESSAGE;
        assertThrows(IllegalValueException.class, expectedMessage, itinerary::toModelType);
    }
}
