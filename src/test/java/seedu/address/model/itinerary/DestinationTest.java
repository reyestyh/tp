package seedu.address.model.itinerary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DestinationTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Destination(null));
    }

    @Test
    public void constructor_invalidDestination_throwsIllegalArgumentException() {
        String emptyDestination = "";
        assertThrows(IllegalArgumentException.class, () -> new Destination(emptyDestination));

        String spacesOnlyDestination = "   ";
        assertThrows(IllegalArgumentException.class, () -> new Destination(spacesOnlyDestination));
    }

    @Test
    public void isValidDestination() {
        // null destination
        assertThrows(NullPointerException.class, () -> Destination.isValidDestination(null));

        // invalid destinations
        assertFalse(Destination.isValidDestination("")); // empty string
        assertFalse(Destination.isValidDestination(" ")); // spaces only

        // valid destinations
        assertTrue(Destination.isValidDestination("France"));
        assertTrue(Destination.isValidDestination("12345")); // numbers only
        assertTrue(Destination.isValidDestination("Bali, Indonesia")); // with punctuations
    }

    @Test
    public void equals() {
        Destination destination = new Destination("Valid Destination");

        // same values -> returns true
        assertTrue(destination.equals(new Destination("Valid Destination")));

        // case-insensitive
        assertTrue(destination.equals(new Destination("vAlid destiNATion")));

        // same object -> returns true
        assertTrue(destination.equals(destination));

        // null -> returns false
        assertFalse(destination.equals(null));

        // different types -> returns false
        assertFalse(destination.equals(5.0f));

        // different values -> returns false
        assertFalse(destination.equals(new Destination("Other Valid Destination")));
    }
}
