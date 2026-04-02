package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // Contacts

    @Test
    public void execute_validIndexUnfilteredContactList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.DeleteType.CONTACT, INDEX_FIRST);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredContactList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.DeleteType.CONTACT, outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredContactList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.DeleteType.CONTACT, INDEX_FIRST);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredContactList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST);

        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.DeleteType.CONTACT, outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // Itinerary

    @Test
    public void execute_validIndexUnfilteredItineraryList_success() {
        Itinerary itineraryToDelete = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.DeleteType.ITINERARY, INDEX_FIRST);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ITINERARY_SUCCESS,
                Messages.format(itineraryToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteItinerary(itineraryToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredItineraryList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredItineraryList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.DeleteType.ITINERARY, outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstContactCommand = new DeleteCommand(DeleteCommand.DeleteType.CONTACT,
                                                                    INDEX_FIRST);
        DeleteCommand deleteSecondContactCommand = new DeleteCommand(DeleteCommand.DeleteType.CONTACT,
                                                                     INDEX_SECOND);

        // same object -> returns true
        assertTrue(deleteFirstContactCommand.equals(deleteFirstContactCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(DeleteCommand.DeleteType.CONTACT, INDEX_FIRST);
        assertTrue(deleteFirstContactCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstContactCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstContactCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstContactCommand.equals(deleteSecondContactCommand));


        DeleteCommand deleteFirstItineraryCommand = new DeleteCommand(DeleteCommand.DeleteType.ITINERARY,
                                                                      INDEX_FIRST);
        DeleteCommand deleteSecondItineraryCommand = new DeleteCommand(DeleteCommand.DeleteType.ITINERARY,
                                                                       INDEX_SECOND);

        // same object -> returns true
        assertTrue(deleteFirstItineraryCommand.equals(deleteFirstItineraryCommand));

        // same values -> returns true
        DeleteCommand deleteFirstItineraryCommandCopy = new DeleteCommand(DeleteCommand.DeleteType.ITINERARY,
                                                                          INDEX_FIRST);
        assertTrue(deleteFirstItineraryCommand.equals(deleteFirstItineraryCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstItineraryCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstItineraryCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstItineraryCommand.equals(deleteSecondItineraryCommand));


    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.DeleteType.CONTACT, targetIndex);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());

        Index targetIndex2 = Index.fromOneBased(2);
        DeleteCommand deleteCommand2 = new DeleteCommand(DeleteCommand.DeleteType.ITINERARY, targetIndex2);
        String expected2 = DeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex2 + "}";
        assertEquals(expected2, deleteCommand2.toString());

    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

}
