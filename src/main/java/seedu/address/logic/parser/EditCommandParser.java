package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.CONTACT_FLAG;
import static seedu.address.logic.parser.CliSyntax.ITINERARY_FLAG;
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
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditItineraryCommand;
import seedu.address.logic.commands.EditItineraryCommand.EditItineraryDescriptor;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.itinerary.DateRange;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object.
 */
public class EditCommandParser implements Parser<EditCommand> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

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

        if (splitArgs.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        if (!flagStr.equals(CONTACT_FLAG) && !flagStr.equals(ITINERARY_FLAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        String otherArgs = splitArgs[1];

        if (flagStr.equals(CONTACT_FLAG)) {
            return parseEditPersonCommand(otherArgs);
        } else { // itinerary flag
            return parseEditItineraryCommand(otherArgs);
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

    /**
     * Parses {@code Person} details into a {@code EditPersonCommand}.
     */
    private EditPersonCommand parseEditPersonCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor =
                new EditPersonCommand.EditPersonDescriptor();

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
        return new EditPersonCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Itinerary} details into a {@code EditItineraryCommand}.
     */
    private EditItineraryCommand parseEditItineraryCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ITINERARY_NAME, PREFIX_ITINERARY_DESTINATION,
                        PREFIX_ITINERARY_START, PREFIX_ITINERARY_END);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ITINERARY_NAME, PREFIX_ITINERARY_DESTINATION,
                PREFIX_ITINERARY_START, PREFIX_ITINERARY_END);

        EditItineraryDescriptor editItineraryDescriptor =
                new EditItineraryCommand.EditItineraryDescriptor();

        if (argMultimap.getValue(PREFIX_ITINERARY_NAME).isPresent()) {
            editItineraryDescriptor.setItineraryName(
                    ParserUtil.parseItineraryName(argMultimap.getValue(PREFIX_ITINERARY_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_ITINERARY_DESTINATION).isPresent()) {
            editItineraryDescriptor.setDestination(
                    ParserUtil.parseDestination(argMultimap.getValue(PREFIX_ITINERARY_DESTINATION).get()));
        }
        if (argMultimap.getValue(PREFIX_ITINERARY_START).isPresent()) {
            try {
                editItineraryDescriptor.setStartDate(
                        LocalDate.parse(argMultimap.getValue(PREFIX_ITINERARY_START).get(), DATE_FORMAT));
            } catch (DateTimeParseException e) {
                throw new ParseException(DateRange.MESSAGE_CONSTRAINTS);
            }
        }
        if (argMultimap.getValue(PREFIX_ITINERARY_END).isPresent()) {
            try {
                editItineraryDescriptor.setEndDate(
                        LocalDate.parse(argMultimap.getValue(PREFIX_ITINERARY_END).get(), DATE_FORMAT));
            } catch (DateTimeParseException e) {
                throw new ParseException(DateRange.MESSAGE_CONSTRAINTS);
            }
        }
        if (!editItineraryDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditItineraryCommand(index, editItineraryDescriptor);
    }
}
