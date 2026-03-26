package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddcCommand;
import seedu.address.logic.commands.AddiCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditItineraryDescriptor;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ShowCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PersonMatchesFieldsPredicate;
import seedu.address.testutil.EditItineraryDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.ItineraryBuilder;
import seedu.address.testutil.ItineraryUtil;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_addc() throws Exception {
        Person person = new PersonBuilder().build();
        AddcCommand command = (AddcCommand) parser.parseCommand(AddcCommand.COMMAND_WORD + " "
                + PREFIX_ROLE + person.getRole().value + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new AddcCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand deletePersonCommand = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + DeleteCommand.CONTACT_FLAG + " " + INDEX_FIRST.getOneBased());
        assertEquals(new DeleteCommand(DeleteCommand.DeleteType.CONTACT, INDEX_FIRST), deletePersonCommand);

        DeleteCommand deleteItineraryCommand = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + DeleteCommand.ITINERARY_FLAG + " " + INDEX_FIRST.getOneBased());
        assertEquals(new DeleteCommand(DeleteCommand.DeleteType.ITINERARY, INDEX_FIRST), deleteItineraryCommand);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor personDescriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand editPersonCommand = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + EditCommand.CONTACT_FLAG + " "
                + INDEX_FIRST.getOneBased() + " "
                + PersonUtil.getEditPersonDescriptorDetails(personDescriptor));
        assertEquals(new EditCommand(INDEX_FIRST, EditCommand.EditType.CONTACT,
                personDescriptor, null), editPersonCommand);

        Itinerary itinerary = new ItineraryBuilder().build();
        EditItineraryDescriptor itineraryDescriptor = new EditItineraryDescriptorBuilder(itinerary).build();
        EditCommand editItineraryCommand = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + EditCommand.ITINERARY_FLAG + " "
                + INDEX_FIRST.getOneBased() + " "
                + ItineraryUtil.getEditItineraryDescriptorDetails(itineraryDescriptor));
        assertEquals(new EditCommand(INDEX_FIRST, EditCommand.EditType.ITINERARY,
                null, itineraryDescriptor), editItineraryCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findWithPrefixes() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " n/foo p/123456");

        assertEquals(new FindCommand(new PersonMatchesFieldsPredicate(
                Collections.singletonList("foo"),
                Collections.singletonList("123456"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        )), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " /all") instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " /contact") instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " /client") instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " /vendor") instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " /itinerary") instanceof ListCommand);
    }

    @Test
    public void parseCommand_addi() throws Exception {
        Itinerary itinerary = new ItineraryBuilder().build();
        AddiCommand command = (AddiCommand) parser.parseCommand("addi n/5D4N Trip to France dest/France "
                                                                + "from/2024-12-01 to/2024-12-05");
        assertEquals(new AddiCommand(itinerary, new HashSet<>(), new HashSet<>()), command);
    }

    @Test
    public void parseCommand_show() throws Exception {
        ShowCommand command = (ShowCommand) parser.parseCommand(ShowCommand.COMMAND_WORD + " 1");
        assertEquals(new ShowCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
