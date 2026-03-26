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

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditItineraryDescriptor;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
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
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT, personDescriptor, null);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));
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
        EditCommand editCommand = new EditCommand(indexLastPerson, EditCommand.EditType.CONTACT,
                personDescriptor, null);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personNoFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT,
                new EditPersonDescriptor(), null);
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build(), null);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personDuplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        EditPersonDescriptor personDescriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND, EditCommand.EditType.CONTACT, personDescriptor, null);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_personDuplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT,
                new EditPersonDescriptorBuilder(personInList).build(), null);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_personInvalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor personDescriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, EditCommand.EditType.CONTACT,
                personDescriptor, null);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_itineraryAllFieldsSpecifiedUnfilteredList_success() {
        Itinerary editedItinerary = new ItineraryBuilder().build();
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder(editedItinerary).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                null, itineraryDescriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
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
        EditCommand editCommand = new EditCommand(indexLastItinerary, EditCommand.EditType.ITINERARY,
                null, itineraryDescriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setItinerary(lastItinerary, editedItinerary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_itineraryNoFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                null, new EditItineraryDescriptor());
        Itinerary editedItinerary = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
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
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                null, new EditItineraryDescriptorBuilder().withName(VALID_ITINERARY_NAME_FRANCE).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ITINERARY_SUCCESS,
                Messages.format(editedItinerary));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setItinerary(model.getFilteredItineraryList().get(0), editedItinerary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_itineraryDuplicateItineraryUnfilteredList_failure() {
        Itinerary firstItinerary = model.getFilteredItineraryList().get(INDEX_FIRST.getZeroBased());
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder(firstItinerary).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND, EditCommand.EditType.ITINERARY,
                null, itineraryDescriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_ITINERARY);
    }

    @Test
    public void execute_itineraryDuplicateItineraryFilteredList_failure() {
        showItineraryAtIndex(model, INDEX_FIRST);

        // edit itinerary in filtered list into a duplicate in address book
        Itinerary itineraryInList = model.getAddressBook().getItineraryList().get(INDEX_SECOND.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                null, new EditItineraryDescriptorBuilder(itineraryInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_ITINERARY);
    }

    @Test
    public void execute_itineraryInvalidItineraryIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredItineraryList().size() + 1);
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, EditCommand.EditType.ITINERARY,
                null, itineraryDescriptor);

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

        EditCommand editCommand = new EditCommand(outOfBoundIndex, EditCommand.EditType.ITINERARY,
                 null, new EditItineraryDescriptorBuilder().withName(VALID_ITINERARY_NAME_BALI).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        // edit person command
        final EditCommand standardPersonCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT,
                DESC_AMY, null);

        // same person values -> returns true
        EditPersonDescriptor copyPersonDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand personCommandWithSameValues = new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT,
                copyPersonDescriptor, null);
        assertTrue(standardPersonCommand.equals(personCommandWithSameValues));

        // same object -> returns true
        assertTrue(standardPersonCommand.equals(standardPersonCommand));

        // null -> returns false
        assertFalse(standardPersonCommand.equals(null));

        // different types -> returns false
        assertFalse(standardPersonCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardPersonCommand.equals(new EditCommand(INDEX_SECOND, EditCommand.EditType.CONTACT,
                DESC_AMY, null)));

        // different flag -> returns false
        assertFalse(standardPersonCommand.equals(new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                DESC_AMY, null)));

        // different personDescriptor -> returns false
        assertFalse(standardPersonCommand.equals(new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT,
                DESC_BOB, null)));

        // edit itinerary command
        final EditCommand standardItineraryCommand = new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                null, DESC_FRANCE);

        // same itinerary values -> returns true
        EditItineraryDescriptor copyItineraryDescriptor = new EditItineraryDescriptor(DESC_FRANCE);
        EditCommand itineraryCommandWithSameValues = new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                null, copyItineraryDescriptor);
        assertTrue(standardItineraryCommand.equals(itineraryCommandWithSameValues));

        // same object -> returns true
        assertTrue(standardItineraryCommand.equals(standardItineraryCommand));

        // null -> returns false
        assertFalse(standardItineraryCommand.equals(null));

        // different types -> returns false
        assertFalse(standardItineraryCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardItineraryCommand.equals(new EditCommand(INDEX_SECOND, EditCommand.EditType.ITINERARY,
                null, DESC_FRANCE)));

        // different flag -> returns false
        assertFalse(standardItineraryCommand.equals(new EditCommand(INDEX_SECOND, EditCommand.EditType.CONTACT,
                null, DESC_FRANCE)));

        // different itineraryDescriptor -> returns false
        assertFalse(standardItineraryCommand.equals(new EditCommand(INDEX_SECOND, EditCommand.EditType.ITINERARY,
                null, DESC_BALI)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, EditCommand.EditType.CONTACT, editPersonDescriptor,
                null);
        String expected = EditCommand.class.getCanonicalName()
                + "{index=" + index
                + ", flag=" + EditCommand.EditType.CONTACT
                + ", editPersonDescriptor=" + editPersonDescriptor
                + ", editItineraryDescriptor=" + null
                + "}";
        assertEquals(expected, editCommand.toString());
    }
}
