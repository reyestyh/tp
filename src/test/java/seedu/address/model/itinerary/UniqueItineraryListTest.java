package seedu.address.model.itinerary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_DEST_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_END_DATE_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_START_DATE_BALI;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalItineraries.BALI_TRIP;
import static seedu.address.testutil.TypicalItineraries.FRANCE_TRIP;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.itinerary.exceptions.DuplicateItineraryException;
import seedu.address.model.itinerary.exceptions.ItineraryNotFoundException;
import seedu.address.model.person.UniquePersonList;
import seedu.address.testutil.ItineraryBuilder;

public class UniqueItineraryListTest {

    private final UniqueItineraryList uniqueItineraryList = new UniqueItineraryList();

    @Test
    public void contains_nullItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueItineraryList.contains(null));
    }

    @Test
    public void contains_itineraryNotInList_returnsFalse() {
        assertFalse(uniqueItineraryList.contains(FRANCE_TRIP));
    }

    @Test
    public void contains_itineraryInList_returnsTrue() {
        uniqueItineraryList.add(FRANCE_TRIP);
        assertTrue(uniqueItineraryList.contains(FRANCE_TRIP));
    }

    @Test
    public void contains_itineraryWithSameIdentityFieldsInList_returnsTrue() {
        uniqueItineraryList.add(FRANCE_TRIP);
        Itinerary editedItinerary = new ItineraryBuilder(FRANCE_TRIP)
                .withDestination(VALID_ITINERARY_DEST_BALI)
                .withDateRange(VALID_ITINERARY_START_DATE_BALI, VALID_ITINERARY_END_DATE_BALI)
                .build();
        assertTrue(uniqueItineraryList.contains(editedItinerary));
    }

    @Test
    public void add_nullItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueItineraryList.add(null));
    }

    @Test
    public void add_duplicateItinerary_throwsDuplicateItineraryException() {
        uniqueItineraryList.add(FRANCE_TRIP);
        assertThrows(DuplicateItineraryException.class, () -> uniqueItineraryList.add(FRANCE_TRIP));
    }

    @Test
    public void setItinerary_nullTargetItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueItineraryList.setItinerary(null, FRANCE_TRIP));
    }

    @Test
    public void setItinerary_nullEditedItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueItineraryList.setItinerary(FRANCE_TRIP, null));
    }

    @Test
    public void setItinerary_targetItineraryNotInList_throwsItineraryNotFoundException() {
        assertThrows(ItineraryNotFoundException.class, () -> uniqueItineraryList
                .setItinerary(FRANCE_TRIP, FRANCE_TRIP));
    }

    @Test
    public void setItinerary_editedItineraryIsSameItinerary_success() {
        uniqueItineraryList.add(FRANCE_TRIP);
        uniqueItineraryList.setItinerary(FRANCE_TRIP, FRANCE_TRIP);
        UniqueItineraryList expectedUniqueItineraryList = new UniqueItineraryList();
        expectedUniqueItineraryList.add(FRANCE_TRIP);
        assertEquals(expectedUniqueItineraryList, uniqueItineraryList);
    }

    @Test
    public void setItinerary_editedItineraryHasSameIdentity_success() {
        uniqueItineraryList.add(FRANCE_TRIP);
        Itinerary editedItinerary = new ItineraryBuilder(FRANCE_TRIP)
                .withDestination(VALID_ITINERARY_DEST_BALI)
                .withDateRange(VALID_ITINERARY_START_DATE_BALI, VALID_ITINERARY_END_DATE_BALI)
                .build();
        uniqueItineraryList.setItinerary(FRANCE_TRIP, editedItinerary);
        UniqueItineraryList expectedUniqueItineraryList = new UniqueItineraryList();
        expectedUniqueItineraryList.add(editedItinerary);
        assertEquals(expectedUniqueItineraryList, uniqueItineraryList);
    }

    @Test
    public void setItinerary_editedItineraryHasDifferentIdentity_success() {
        uniqueItineraryList.add(FRANCE_TRIP);
        uniqueItineraryList.set(FRANCE_TRIP, BALI_TRIP);
        UniqueItineraryList expectedUniqueItineraryList = new UniqueItineraryList();
        expectedUniqueItineraryList.add(BALI_TRIP);
        assertEquals(expectedUniqueItineraryList, uniqueItineraryList);
    }

    @Test
    public void setItinerary_editedItineraryHasNonUniqueIdentity_success() {
        uniqueItineraryList.add(FRANCE_TRIP);
        uniqueItineraryList.add(BALI_TRIP);
        assertThrows(DuplicateItineraryException.class, () -> uniqueItineraryList.setItinerary(FRANCE_TRIP, BALI_TRIP));
    }

    @Test
    public void remove_nullItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueItineraryList.remove(null));
    }

    @Test
    public void remove_itineraryDoesNotExist_throwsItineraryNotFoundException() {
        assertThrows(ItineraryNotFoundException.class, () -> uniqueItineraryList.remove(FRANCE_TRIP));
    }

    @Test
    public void remove_existingItinerary_removesItinerary() {
        uniqueItineraryList.add(FRANCE_TRIP);
        uniqueItineraryList.remove(FRANCE_TRIP);
        UniqueItineraryList expectedUniqueItineraryList = new UniqueItineraryList();
        assertEquals(expectedUniqueItineraryList, uniqueItineraryList);
    }

    @Test
    public void setItineraries_nullUniqueItineraryList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueItineraryList.setItineraries((UniqueItineraryList) null));
    }

    @Test
    public void setItineraries_uniqueItineraryList_replacesOwnListWithProvidedUniqueItineraryList() {
        uniqueItineraryList.add(FRANCE_TRIP);
        UniqueItineraryList expectedUniqueItineraryList = new UniqueItineraryList();
        expectedUniqueItineraryList.add(BALI_TRIP);
        uniqueItineraryList.setItineraries(expectedUniqueItineraryList);
        assertEquals(expectedUniqueItineraryList, uniqueItineraryList);
    }

    @Test
    public void setItineraries_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueItineraryList.setItineraries((List<Itinerary>) null));
    }

    @Test
    public void setItineraries_list_replacesOwnListWithProvidedList() {
        uniqueItineraryList.add(FRANCE_TRIP);
        List<Itinerary> itineraryList = Collections.singletonList(BALI_TRIP);
        uniqueItineraryList.setItineraries(itineraryList);
        UniqueItineraryList expectedUniqueItineraryList = new UniqueItineraryList();
        expectedUniqueItineraryList.add(BALI_TRIP);
        assertEquals(expectedUniqueItineraryList, uniqueItineraryList);
    }

    @Test
    public void setItineraries_listWithDuplicateItineraries_throwsDuplicateItineraryException() {
        List<Itinerary> listWithDuplicateItineraries = Arrays.asList(FRANCE_TRIP, FRANCE_TRIP);
        assertThrows(DuplicateItineraryException.class, ()
                -> uniqueItineraryList.setItineraries(listWithDuplicateItineraries));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueItineraryList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueItineraryList.asUnmodifiableObservableList().toString(), uniqueItineraryList.toString());
    }

    @Test
    public void equals() {
        uniqueItineraryList.add(FRANCE_TRIP);

        // same values -> returns true
        UniqueItineraryList uniqueItineraryListCopy = new UniqueItineraryList();
        uniqueItineraryListCopy.add(FRANCE_TRIP);
        assertTrue(uniqueItineraryList.equals(uniqueItineraryListCopy));

        // same object -> returns true
        assertTrue(uniqueItineraryList.equals(uniqueItineraryList));

        // null -> returns false
        assertFalse(uniqueItineraryList.equals(null));

        // empty UniqueItineraryList and empty UniquePersonList -> returns false
        assertFalse(new UniqueItineraryList().equals(new UniquePersonList()));

        assertFalse(uniqueItineraryList.equals(new UniqueItineraryList()));

        // different values -> returns false
        UniqueItineraryList uniqueItineraryDifferent = new UniqueItineraryList();
        uniqueItineraryDifferent.add(BALI_TRIP);
        assertFalse(uniqueItineraryList.equals(uniqueItineraryDifferent));
    }
}

