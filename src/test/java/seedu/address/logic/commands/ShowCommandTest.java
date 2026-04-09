package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showItineraryAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.id.Id;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryMatchesPredicate;
import seedu.address.model.person.IdMatchesPredicate;

public class ShowCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        ShowCommand showCommand = new ShowCommand(INDEX_FIRST);
        Itinerary itineraryToShow = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        String expectedMessage = String.format(ShowCommand.MESSAGE_SUCCESS, itineraryToShow.getName());
        updateModelFilteredList(expectedModel, itineraryToShow);
        assertCommandSuccess(showCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ShowCommand showCommand = new ShowCommand(outOfBoundIndex);
        assertCommandFailure(showCommand, model, Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showItineraryAtIndex(model, INDEX_FIRST);

        ShowCommand showCommand = new ShowCommand(INDEX_FIRST);
        Itinerary itineraryToShow = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        String expectedMessage = String.format(ShowCommand.MESSAGE_SUCCESS, itineraryToShow.getName());
        updateModelFilteredList(expectedModel, itineraryToShow);
        assertCommandSuccess(showCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showItineraryAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getItineraryList().size());

        ShowCommand showCommand = new ShowCommand(outOfBoundIndex);
        assertCommandFailure(showCommand, model, Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
    }

    private void updateModelFilteredList(Model model, Itinerary itinerary) {
        ItineraryMatchesPredicate itineraryMatchesPredicate = new ItineraryMatchesPredicate(
                List.of(itinerary));
        model.updateFilteredItineraryList(itineraryMatchesPredicate);

        List<Id> ids = Stream.concat(
                        itinerary.getClientIds().stream(),
                        itinerary.getVendorIds().stream())
                .collect(Collectors.toList());
        IdMatchesPredicate idMatchesPredicate = new IdMatchesPredicate(ids);
        model.updateFilteredPersonList(idMatchesPredicate);
    }

    @Test
    public void equals() {
        ShowCommand showCommand = new ShowCommand(INDEX_FIRST);

        // same object -> returns true
        assertTrue(showCommand.equals(showCommand));

        // same index -> returns true
        assertTrue(showCommand.equals(new ShowCommand(Index.fromOneBased(1))));

        // different types -> returns false
        assertFalse(showCommand.equals(1));

        // null -> returns false
        assertFalse(showCommand.equals(null));

        // different index -> returns false
        assertFalse(showCommand.equals(new ShowCommand(INDEX_SECOND)));
    }

    @Test
    public void toStringMethod() {
        ShowCommand showCommand = new ShowCommand(INDEX_FIRST);
        String expected = ShowCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST + "}";
        assertEquals(expected, showCommand.toString());
    }
}
