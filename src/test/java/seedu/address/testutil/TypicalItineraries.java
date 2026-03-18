package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_DEST_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_END_DATE_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_START_DATE_BALI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.itinerary.Itinerary;

/**
 * A utility class containing a list of {@code Itinerary} objects to be used in tests.
 */
public class TypicalItineraries {

    public static final Itinerary AUSTRALIA_TRIP = new ItineraryBuilder().withName("G'day mate")
            .withDestination("Australia").withDateRange("2026-05-01", "2026-05-10").build();

    public static final Itinerary FRANCE_TRIP = new ItineraryBuilder().build();
    public static final Itinerary BALI_TRIP = new ItineraryBuilder().withName(VALID_ITINERARY_NAME_BALI)
            .withDestination(VALID_ITINERARY_DEST_BALI).withDateRange(VALID_ITINERARY_START_DATE_BALI,
                    VALID_ITINERARY_END_DATE_BALI).build();


    private TypicalItineraries() {} // prevents instantiation

    public static List<Itinerary> getTypicalItineraries() {
        return new ArrayList<>(Arrays.asList(FRANCE_TRIP, BALI_TRIP));
    }
}
