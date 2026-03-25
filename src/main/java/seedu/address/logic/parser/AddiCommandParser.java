package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_CLIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_DESTINATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_START;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_VENDOR;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddiCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.itinerary.DateRange;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryName;

/**
 * Parses input arguments and creates a new AddiCommand object.
 */
public class AddiCommandParser implements Parser<AddiCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddiCommand
     * and returns an AddiCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddiCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ITINERARY_NAME, PREFIX_ITINERARY_DESTINATION,
                                           PREFIX_ITINERARY_START, PREFIX_ITINERARY_END, PREFIX_ITINERARY_CLIENT,
                                           PREFIX_ITINERARY_VENDOR);

        if (!arePrefixesPresent(argMultimap, PREFIX_ITINERARY_NAME, PREFIX_ITINERARY_DESTINATION,
                                PREFIX_ITINERARY_START, PREFIX_ITINERARY_END)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddiCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ITINERARY_NAME, PREFIX_ITINERARY_DESTINATION,
                                                 PREFIX_ITINERARY_START, PREFIX_ITINERARY_END);
        ItineraryName name = ParserUtil.parseItineraryName(argMultimap.getValue(PREFIX_ITINERARY_NAME).get());
        Destination destination = ParserUtil.parseDestination(argMultimap.getValue(PREFIX_ITINERARY_DESTINATION).get());
        DateRange dateRange = ParserUtil.parseItineraryDates(argMultimap.getValue(PREFIX_ITINERARY_START).get(),
                                                             argMultimap.getValue(PREFIX_ITINERARY_END).get());
        Set<Index> clientIndices = ParserUtil.parseIndices(argMultimap.getAllValues(PREFIX_ITINERARY_CLIENT));
        Set<Index> vendorIndices = ParserUtil.parseIndices(argMultimap.getAllValues(PREFIX_ITINERARY_VENDOR));


        Itinerary itinerary = new Itinerary(name, destination, dateRange);

        return new AddiCommand(itinerary, clientIndices, vendorIndices);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
