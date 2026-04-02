package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showItineraryAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAndItineraryAtIndexes;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalItineraries.getTypicalItineraries;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;
import seedu.address.ui.PanelType;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void equals() {
        ListCommand listContactCommand = new ListCommand(ListCommand.Flag.CONTACT);
        ListCommand listClientCommand = new ListCommand(ListCommand.Flag.CLIENT);

        // same object -> returns true
        assertTrue(listContactCommand.equals(listContactCommand));

        // same flag -> returns true
        ListCommand listContactCommandCopy = new ListCommand(ListCommand.Flag.CONTACT);
        assertTrue(listContactCommand.equals(listContactCommandCopy));

        // different types -> returns false
        assertFalse(listContactCommand.equals(1));

        // null -> returns false
        assertFalse(listContactCommand.equals(null));

        // different flag -> returns false
        assertFalse(listContactCommand.equals(listClientCommand));
    }

    @Test
    public void execute_allListIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(ListCommand.Flag.ALL), model,
                ListCommand.MESSAGE_SUCCESS_ALL, expectedModel);
    }

    @Test
    public void execute_allListIsFiltered_showsEverything() {
        showPersonAndItineraryAtIndexes(model, INDEX_FIRST, INDEX_FIRST);
        assertCommandSuccess(new ListCommand(ListCommand.Flag.ALL), model,
                ListCommand.MESSAGE_SUCCESS_ALL, expectedModel);
    }

    @Test
    public void execute_contactListIsNotFiltered_showsContactsOnly() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.CONTACT).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_CONTACTS, result.getFeedbackToUser());
        assertEquals(getTypicalPersons().size(), model.getFilteredPersonList().size());
    }

    @Test
    public void execute_contactListIsFiltered_showsContactsOnly() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST);
        CommandResult result = new ListCommand(ListCommand.Flag.CONTACT).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_CONTACTS, result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().stream().allMatch(p -> p.getClass().equals(Person.class)));
    }

    @Test
    public void execute_clientListIsNotFiltered_showsClientsOnly() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.CLIENT).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_CLIENTS, result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().stream()
                .allMatch(p -> p.getRole().equals(new Role("client"))));
    }

    @Test
    public void execute_clientListIsFiltered_showsClientsOnly() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST);
        CommandResult result = new ListCommand(ListCommand.Flag.CLIENT).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_CLIENTS, result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().stream()
                .allMatch(p -> p.getRole().equals(new Role("client"))));
    }

    @Test
    public void execute_vendorListIsNotFiltered_showsVendorsOnly() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.VENDOR).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_VENDORS, result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().stream()
                .allMatch(p -> p.getRole().equals(new Role("vendor"))));
    }

    @Test
    public void execute_vendorListIsFiltered_showsVendorsOnly() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST);
        CommandResult result = new ListCommand(ListCommand.Flag.VENDOR).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_VENDORS, result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().stream()
                .allMatch(p -> p.getRole().equals(new Role("vendor"))));
    }

    @Test
    public void execute_itineraryListIsNotFiltered_showsSameList() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.ITINERARY).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_ITINERARIES, result.getFeedbackToUser());
        assertEquals(getTypicalItineraries().size(),
                model.getFilteredItineraryList().size());
    }

    @Test
    public void execute_itineraryListIsFiltered_showsSameList() throws CommandException {
        showItineraryAtIndex(model, INDEX_FIRST);
        CommandResult result = new ListCommand(ListCommand.Flag.ITINERARY).execute(model);
        assertEquals(ListCommand.MESSAGE_SUCCESS_ITINERARIES, result.getFeedbackToUser());
        assertEquals(getTypicalItineraries().size(),
                model.getFilteredItineraryList().size());
    }

    @Test
    public void execute_allList_showsBothPanels() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.ALL).execute(model);
        assertEquals(PanelType.BOTH, result.getPanelType());
    }

    @Test
    public void execute_contactList_showsContactPanel() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.CONTACT).execute(model);
        assertEquals(PanelType.CONTACT, result.getPanelType());
    }

    @Test
    public void execute_clientList_showsContactPanel() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.CLIENT).execute(model);
        assertEquals(PanelType.CONTACT, result.getPanelType());
    }

    @Test
    public void execute_vendorList_showsContactPanel() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.VENDOR).execute(model);
        assertEquals(PanelType.CONTACT, result.getPanelType());
    }

    @Test
    public void execute_itineraryList_showsItineraryPanel() throws CommandException {
        CommandResult result = new ListCommand(ListCommand.Flag.ITINERARY).execute(model);
        assertEquals(PanelType.ITINERARY, result.getPanelType());
    }

    @Test
    public void toStringMethod() {
        ListCommand listCommand = new ListCommand(ListCommand.Flag.CONTACT);
        String expected = ListCommand.class.getCanonicalName() + "{flag=" + ListCommand.Flag.CONTACT + "}";
        assertEquals(expected, listCommand.toString());
    }
}
