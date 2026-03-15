package seedu.address.model.itinerary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_DEST_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_END_DATE_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_START_DATE_BALI;
import static seedu.address.testutil.TypicalItinerary.TRIP_TO_FRANCE;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ItineraryBuilder;

public class ItineraryTest {

    @Test
    public void isSameItinerary() {
        // same object -> returns true
        assertTrue(TRIP_TO_FRANCE.isSameItinerary(TRIP_TO_FRANCE));

        // null -> returns false
        assertFalse(TRIP_TO_FRANCE.isSameItinerary(null));

        // same name, all other attributes different -> returns true
        Itinerary editedItinerary = new ItineraryBuilder(TRIP_TO_FRANCE).withDestination(VALID_ITINERARY_DEST_BALI)
                .withDateRange(VALID_ITINERARY_START_DATE_BALI, VALID_ITINERARY_END_DATE_BALI).build();
        assertTrue(TRIP_TO_FRANCE.isSameItinerary(editedItinerary));

        // different name, all other attributes same -> returns false
        editedItinerary = new ItineraryBuilder().withName(VALID_ITINERARY_NAME_BALI).build();
        assertFalse(TRIP_TO_FRANCE.isSameItinerary(editedItinerary));

        // name differs in case, all other attributes same -> returns true
        Itinerary editedtripToFrance = new ItineraryBuilder(TRIP_TO_FRANCE)
                .withName(VALID_ITINERARY_NAME_FRANCE.toLowerCase()).build();
        assertTrue(TRIP_TO_FRANCE.isSameItinerary(editedtripToFrance));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_ITINERARY_NAME_FRANCE + " ";
        editedtripToFrance = new ItineraryBuilder(TRIP_TO_FRANCE).withName(nameWithTrailingSpaces).build();
        assertFalse(TRIP_TO_FRANCE.isSameItinerary(editedtripToFrance));
    }

    @Test
    public void toStringMethod() {
        String expected = Itinerary.class.getCanonicalName() + "{itineraryName=" + TRIP_TO_FRANCE.getName()
                + ", destination=" + TRIP_TO_FRANCE.getDestination()
                + ", date range=" + TRIP_TO_FRANCE.getDateRange() + "}";
        assertEquals(expected, TRIP_TO_FRANCE.toString());
    }
}
