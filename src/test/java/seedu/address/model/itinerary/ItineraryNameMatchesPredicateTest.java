package seedu.address.model.itinerary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ItineraryBuilder;

public class ItineraryNameMatchesPredicateTest {

    @Test
    public void equals() {
        List<ItineraryName> firstPredicateItineraryNameList = Collections.singletonList(
                new ItineraryName("5D4N France Trip"));
        List<ItineraryName> secondPredicateItineraryNameList = Arrays.asList(
                new ItineraryName("5D4N France Trip"),
                new ItineraryName("Island Time: Bali"));

        ItineraryNameMatchesPredicate firstPredicate = new ItineraryNameMatchesPredicate(
                firstPredicateItineraryNameList);
        ItineraryNameMatchesPredicate secondPredicate = new ItineraryNameMatchesPredicate(
                secondPredicateItineraryNameList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ItineraryNameMatchesPredicate firstPredicateCopy = new ItineraryNameMatchesPredicate(
                firstPredicateItineraryNameList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different values -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameMatches_returnsTrue() {
        // One name
        Itinerary itinerary = new ItineraryBuilder().build();
        ItineraryNameMatchesPredicate predicate = new ItineraryNameMatchesPredicate(
                Collections.singletonList(itinerary.getName()));
        assertTrue(predicate.test(itinerary));

        // Multiple names
        predicate = new ItineraryNameMatchesPredicate(Arrays.asList(
                itinerary.getName(), new ItineraryName("Island Time: Bali")));
        assertTrue(predicate.test(itinerary));
    }

    @Test
    public void test_nameDoesNotMatch_returnsFalse() {
        Itinerary itinerary = new ItineraryBuilder().build();
        // Zero names
        ItineraryNameMatchesPredicate predicate = new ItineraryNameMatchesPredicate(Collections.emptyList());
        assertFalse(predicate.test(itinerary));

        // Non-matching name
        predicate = new ItineraryNameMatchesPredicate(Collections.singletonList(
                new ItineraryName("Island Time: Bali")));
        assertFalse(predicate.test(itinerary));
    }

    @Test
    public void toStringMethod() {
        List<ItineraryName> names = List.of(new ItineraryName("name 1"), new ItineraryName("name 2"));
        ItineraryNameMatchesPredicate predicate = new ItineraryNameMatchesPredicate(names);

        String expected = ItineraryNameMatchesPredicate.class.getCanonicalName() + "{names=" + names + "}";
        assertEquals(expected, predicate.toString());
    }
}
