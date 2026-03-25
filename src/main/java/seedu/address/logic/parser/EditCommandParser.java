package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_DESTINATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_START;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object.
 */
public class EditCommandParser implements Parser<EditCommand> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final String CONTACT_FLAG = "/contact";
    public static final String ITINERARY_FLAG = "/itinerary";

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        String[] splitArgs = trimmedArgs.split("\\s+", 2);
        String flagStr = splitArgs[0].toLowerCase(); // case-insensitive
        EditCommand.EditType flag;

        if (splitArgs.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        if (!flagStr.equals(CONTACT_FLAG) && !flagStr.equals(ITINERARY_FLAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        if (flagStr.equals(CONTACT_FLAG)) {
            flag = EditCommand.EditType.CONTACT;
        } else {
            flag = EditCommand.EditType.ITINERARY;
        }

        String otherArgs = splitArgs[1];

        if (flag == EditCommand.EditType.CONTACT) {
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(otherArgs, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

            Index index;

            try {
                index = ParserUtil.parseIndex(argMultimap.getPreamble());
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
            }

            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

            EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

            if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
            }
            if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
                editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
            }
            if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
                editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
            }
            if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
                editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
            }
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

            if (!editPersonDescriptor.isAnyFieldEdited()) {
                throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
            }

            return new EditCommand(index, flag, editPersonDescriptor, null);
        } else { // itinerary flag
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(otherArgs, PREFIX_ITINERARY_NAME, PREFIX_ITINERARY_DESTINATION,
                            PREFIX_ITINERARY_START, PREFIX_ITINERARY_END);

            Index index;

            try {
                index = ParserUtil.parseIndex(argMultimap.getPreamble());
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
            }

            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ITINERARY_NAME, PREFIX_ITINERARY_DESTINATION,
                    PREFIX_ITINERARY_START, PREFIX_ITINERARY_END);

            EditCommand.EditItineraryDescriptor editItineraryDescriptor = new EditCommand.EditItineraryDescriptor();

            if (argMultimap.getValue(PREFIX_ITINERARY_NAME).isPresent()) {
                editItineraryDescriptor.setItineraryName(ParserUtil.parseItineraryName(argMultimap.getValue(PREFIX_ITINERARY_NAME).get()));
            }
            if (argMultimap.getValue(PREFIX_ITINERARY_DESTINATION).isPresent()) {
                editItineraryDescriptor.setDestination(ParserUtil.parseDestination(argMultimap.getValue(PREFIX_ITINERARY_DESTINATION).get()));
            }
            if (argMultimap.getValue(PREFIX_ITINERARY_START).isPresent()) {
                editItineraryDescriptor.setStartDate(LocalDate.parse(argMultimap.getValue(PREFIX_ITINERARY_START).get(), DATE_FORMAT));
            }
            if (argMultimap.getValue(PREFIX_ITINERARY_END).isPresent()) {
                editItineraryDescriptor.setEndDate(LocalDate.parse(argMultimap.getValue(PREFIX_ITINERARY_END).get(), DATE_FORMAT));
            }
            if (!editItineraryDescriptor.isAnyFieldEdited()) {
                throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
            }

            return new EditCommand(index, flag, null, editItineraryDescriptor);
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
