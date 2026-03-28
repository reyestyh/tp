package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BALI;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_DEST_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showItineraryAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditItineraryCommand.EditItineraryDescriptor;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditItineraryDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.ItineraryBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_personAllFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor personDescriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditPersonCommand(INDEX_FIRST, personDescriptor);

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personSomeFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor personDescriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditPersonCommand(indexLastPerson, personDescriptor);

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personNoFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditPersonCommand(INDEX_FIRST, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditPersonCommand(INDEX_FIRST,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personDuplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        EditPersonDescriptor personDescriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditPersonCommand(INDEX_SECOND, personDescriptor);

        assertCommandFailure(editCommand, model, EditPersonCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_personDuplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND.getZeroBased());
        EditCommand editCommand = new EditPersonCommand(INDEX_FIRST,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditPersonCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_personInvalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor personDescriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditPersonCommand(outOfBoundIndex, personDescriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_itineraryAllFieldsSpecifiedUnfilteredList_success() {
        Itinerary editedItinerary = new ItineraryBuilder().build();
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder(editedItinerary).build();
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST, itineraryDescriptor);

        String expectedMessage = String.format(EditItineraryCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setItinerary(model.getFilteredItineraryList().get(0), editedItinerary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_itinerarySomeFieldsSpecifiedUnfilteredList_success() {
        Index indexLastItinerary = Index.fromOneBased(model.getFilteredItineraryList().size());
        Itinerary lastItinerary = model.getFilteredItineraryList().get(indexLastItinerary.getZeroBased());

        ItineraryBuilder itineraryInList = new ItineraryBuilder(lastItinerary);
        Itinerary editedItinerary = itineraryInList.withName(VALID_ITINERARY_NAME_BALI)
                .withDestination(VALID_ITINERARY_DEST_BALI).build();

        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder()
                .withName(VALID_ITINERARY_NAME_BALI)
                .withDestination(VALID_ITINERARY_DEST_BALI).build();
        EditCommand editCommand = new EditItineraryCommand(indexLastItinerary, itineraryDescriptor);

        String expectedMessage = String.format(EditItineraryCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setItinerary(lastItinerary, editedItinerary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_itineraryNoFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST, new EditItineraryDescriptor());
        Itinerary editedItinerary = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(EditItineraryCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_itineraryFilteredList_success() {
        showItineraryAtIndex(model, INDEX_FIRST);

        Itinerary itineraryInFilteredList = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        Itinerary editedItinerary = new ItineraryBuilder(itineraryInFilteredList)
                .withName(VALID_ITINERARY_NAME_FRANCE).build();
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST,
                new EditItineraryDescriptorBuilder().withName(VALID_ITINERARY_NAME_FRANCE).build());

        String expectedMessage = String.format(EditItineraryCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setItinerary(model.getFilteredItineraryList().get(0), editedItinerary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_itineraryDuplicateItineraryUnfilteredList_failure() {
        Itinerary firstItinerary = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder(firstItinerary).build();
        EditCommand editCommand = new EditItineraryCommand(INDEX_SECOND, itineraryDescriptor);

        assertCommandFailure(editCommand, model, EditItineraryCommand.MESSAGE_DUPLICATE_ITINERARY);
    }

    @Test
    public void execute_itineraryDuplicateItineraryFilteredList_failure() {
        showItineraryAtIndex(model, INDEX_FIRST);

        // edit itinerary in filtered list into a duplicate in address book
        Itinerary itineraryInList = model.getAddressBook().getItineraryList().get(INDEX_SECOND.getZeroBased());
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST,
                new EditItineraryDescriptorBuilder(itineraryInList).build());

        assertCommandFailure(editCommand, model, EditItineraryCommand.MESSAGE_DUPLICATE_ITINERARY);
    }

    @Test
    public void execute_itineraryInvalidItineraryIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredItineraryList().size() + 1);
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditItineraryCommand(outOfBoundIndex, itineraryDescriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_itineraryInvalidItineraryIndexFilteredList_failure() {
        showItineraryAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getItineraryList().size());

        EditCommand editCommand = new EditItineraryCommand(outOfBoundIndex,
                new EditItineraryDescriptorBuilder().withName(VALID_ITINERARY_NAME_BALI).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_itineraryEditStartBeforeEnd_failure() {
        Itinerary itineraryToEdit = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        LocalDate startDateAfterEndDate = itineraryToEdit.getDateRange().getEndDate().plusDays(1);
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder()
                .withStartDate(startDateAfterEndDate).build();
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST, itineraryDescriptor);

        assertCommandFailure(editCommand, model, EditItineraryCommand.MESSAGE_INVALID_START_DATE);
    }

    @Test
    public void execute_itineraryEditEndBeforeStart_failure() {
        Itinerary itineraryToEdit = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        LocalDate endDateAfterStartDate = itineraryToEdit.getDateRange().getStartDate().minusDays(1);
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder()
                .withEndDate(endDateAfterStartDate).build();
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST, itineraryDescriptor);

        assertCommandFailure(editCommand, model, EditItineraryCommand.MESSAGE_INVALID_END_DATE);
    }

    @Test
    public void execute_itineraryEditStartEqualEnd_success() {
        Itinerary itinerary = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        LocalDate endDate = itinerary.getDateRange().getEndDate();

        Itinerary editedItinerary = new ItineraryBuilder(itinerary)
                .withDateRange(endDate.toString(), endDate.toString())
                .build();

        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder()
                .withStartDate(endDate).build();
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST, itineraryDescriptor);

        String expectedMessage = String.format(EditItineraryCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setItinerary(itinerary, editedItinerary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_itineraryEditEndEqualStart_success() {
        Itinerary itinerary = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        LocalDate startDate = itinerary.getDateRange().getEndDate();

        Itinerary editedItinerary = new ItineraryBuilder(itinerary)
                .withDateRange(startDate.toString(), startDate.toString())
                .build();

        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder()
                .withStartDate(startDate).build();
        EditCommand editCommand = new EditItineraryCommand(INDEX_FIRST, itineraryDescriptor);

        String expectedMessage = String.format(EditItineraryCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setItinerary(itinerary, editedItinerary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        final EditPersonCommand standardPersonCommand = new EditPersonCommand(INDEX_FIRST, DESC_AMY);
        final EditCommand standardItineraryCommand = new EditItineraryCommand(INDEX_FIRST, DESC_FRANCE);

        // different object -> returns false
        assertFalse(standardPersonCommand.equals(standardItineraryCommand));

        // edit person command tests

        // same person values -> returns true
        EditPersonDescriptor copyPersonDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand personCommandWithSameValues = new EditPersonCommand(INDEX_FIRST, copyPersonDescriptor);
        assertTrue(standardPersonCommand.equals(personCommandWithSameValues));

        // same object -> returns true
        assertTrue(standardPersonCommand.equals(standardPersonCommand));

        //  -> returns false
        assertFalse(standardPersonCommand.equals(null));

        // different types -> returns false
        assertFalse(standardPersonCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardPersonCommand.equals(new EditPersonCommand(INDEX_SECOND, DESC_AMY)));

        // different personDescriptor -> returns false
        assertFalse(standardPersonCommand.equals(new EditPersonCommand(INDEX_FIRST, DESC_BOB)));

        // edit itinerary command tests

        // same itinerary values -> returns true
        EditItineraryDescriptor copyItineraryDescriptor = new EditItineraryDescriptor(DESC_FRANCE);
        EditCommand itineraryCommandWithSameValues = new EditItineraryCommand(INDEX_FIRST, copyItineraryDescriptor);
        assertTrue(standardItineraryCommand.equals(itineraryCommandWithSameValues));

        // same object -> returns true
        assertTrue(standardItineraryCommand.equals(standardItineraryCommand));

        //  -> returns false
        assertFalse(standardItineraryCommand.equals(null));

        // different types -> returns false
        assertFalse(standardItineraryCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardItineraryCommand.equals(new EditItineraryCommand(INDEX_SECOND, DESC_FRANCE)));

        // different itineraryDescriptor -> returns false
        assertFalse(standardItineraryCommand.equals(new EditItineraryCommand(INDEX_SECOND, DESC_BALI)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditPersonCommand editPersonCommand = new EditPersonCommand(index, editPersonDescriptor);
        String expectedPersonOutput = EditPersonCommand.class.getCanonicalName()
                + "{index=" + index
                + ", editPersonDescriptor=" + editPersonDescriptor
                + "}";
        assertEquals(expectedPersonOutput, editPersonCommand.toString());

        EditItineraryDescriptor editItineraryDescriptor = new EditItineraryDescriptor();
        EditItineraryCommand editItineraryCommand = new EditItineraryCommand(index, editItineraryDescriptor);
        String expectedItineraryOutput = EditItineraryCommand.class.getCanonicalName()
                + "{index=" + index
                + ", editItineraryDescriptor=" + editItineraryDescriptor
                + "}";
        assertEquals(expectedItineraryOutput, editItineraryCommand.toString());
    }
}
