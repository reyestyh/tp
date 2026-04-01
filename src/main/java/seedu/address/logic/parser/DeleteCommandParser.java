package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.CONTACT_FLAG;
import static seedu.address.logic.parser.CliSyntax.ITINERARY_FLAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String[] splitArgs = trimmedArgs.split("\\s+");
        String flagStr = splitArgs[0].toLowerCase(); // case-insensitive
        DeleteCommand.DeleteType flag;

        if (splitArgs.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (!flagStr.equals(CONTACT_FLAG) && !flagStr.equals(ITINERARY_FLAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (flagStr.equals(CONTACT_FLAG)) {
            flag = DeleteCommand.DeleteType.CONTACT;
        } else {
            flag = DeleteCommand.DeleteType.ITINERARY;
        }

        try {
            Index index = ParserUtil.parseIndex(splitArgs[1]);
            return new DeleteCommand(flag, index);
        } catch (ParseException pe) {
            throw pe;
        }
    }

}
