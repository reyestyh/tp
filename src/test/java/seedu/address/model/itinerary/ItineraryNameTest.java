package seedu.address.model.itinerary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ItineraryNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ItineraryName(null));
    }

    @Test void constructor_invalidName_throwsIllegalArgumentException() {
        String emptyName = "";
        assertThrows(IllegalArgumentException.class, () -> new ItineraryName(emptyName));

        String spacesOnlyName = "   ";
        assertThrows(IllegalArgumentException.class, () -> new ItineraryName(spacesOnlyName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> ItineraryName.isValidName(null));

        // invalid name
        assertFalse(ItineraryName.isValidName("")); // empty string
        assertFalse(ItineraryName.isValidName(" ")); // spaces only

        // valid name
        assertTrue(ItineraryName.isValidName("Paris Trip     ")); // alphabets and spaces
        assertTrue(ItineraryName.isValidName("Island time: Bali!!!")); // alphabets and punctuations
        assertTrue(ItineraryName.isValidName("12345")); // numbers only
        assertTrue(ItineraryName.isValidName("5D4N Trip to France")); // alphanumeric characters
    }

    @Test
    public void equals() {
        ItineraryName itineraryName = new ItineraryName("Valid Itinerary ItineraryName");

        // same values -> returns true
        assertTrue(itineraryName.equals(new ItineraryName("Valid Itinerary ItineraryName")));

        // case-insensitive
        assertTrue(itineraryName.equals(new ItineraryName("valid itiNerarY itiNERARYnaMe")));

        // same object -> returns true
        assertTrue(itineraryName.equals(itineraryName));

        // null -> returns false
        assertFalse(itineraryName.equals(null));

        // different types -> returns false
        assertFalse(itineraryName.equals(5.0f));

        // different values -> returns false
        assertFalse(itineraryName.equals(new ItineraryName("Other Valid Itinerary ItineraryName")));
    }
}
