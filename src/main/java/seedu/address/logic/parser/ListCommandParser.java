package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.CONTACT_FLAG;
import static seedu.address.logic.parser.CliSyntax.ITINERARY_FLAG;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object.
 */
public class ListCommandParser implements Parser<ListCommand> {
    public static final String ALL_FLAG = "/all";
    public static final String CLIENT_FLAG = "/client";
    public static final String VENDOR_FLAG = "/vendor";

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedFlag = args.trim();

        // Check for missing flag or extra arguments after the flag
        if (trimmedFlag.isEmpty() || trimmedFlag.contains(" ")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }

        switch (trimmedFlag.toLowerCase()) { // Case-insensitive
        case ALL_FLAG:
            return new ListCommand(ListCommand.Flag.ALL);
        case CONTACT_FLAG:
            return new ListCommand(ListCommand.Flag.CONTACT);
        case CLIENT_FLAG:
            return new ListCommand(ListCommand.Flag.CLIENT);
        case VENDOR_FLAG:
            return new ListCommand(ListCommand.Flag.VENDOR);
        case ITINERARY_FLAG:
            return new ListCommand(ListCommand.Flag.ITINERARY);
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
    }
}
