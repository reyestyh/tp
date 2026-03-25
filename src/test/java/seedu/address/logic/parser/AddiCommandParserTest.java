package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INDEX_CLIENT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INDEX_VENDOR_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_DEST_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_END_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_START_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_CLIENT_IDS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_DEST_DESC_BALI;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_DEST_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_END_DATE_DESC_BALI;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_END_DATE_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_NAME_DESC_BALI;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_NAME_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_START_DATE_DESC_BALI;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_START_DATE_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_VENDOR_IDS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_DEST_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_END_DATE_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_START_DATE_BALI;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;
import static seedu.address.testutil.TypicalItineraries.BALI_TRIP;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddiCommand;
import seedu.address.model.itinerary.DateRange;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryName;
import seedu.address.testutil.ItineraryBuilder;

public class AddiCommandParserTest {

    private AddiCommandParser parser = new AddiCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                new AddiCommand(BALI_TRIP, new HashSet<>(), new HashSet<>()));

        // multiple indices - accepted
        Set<Index> expectedClientIndices = new HashSet<>();
        expectedClientIndices.add(INDEX_FIRST);
        expectedClientIndices.add(INDEX_SECOND);

        Set<Index> expectedVendorIndices = new HashSet<>();
        expectedVendorIndices.add(INDEX_THIRD);
        Itinerary expectedItinerary = new ItineraryBuilder().build();

        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE
                        + ITINERARY_CLIENT_IDS_DESC + ITINERARY_VENDOR_IDS_DESC,
                new AddiCommand(expectedItinerary, expectedClientIndices, expectedVendorIndices));


    }

    @Test
    public void parse_optionalFieldsMissing_success() {

        Itinerary validItinerary = new ItineraryBuilder().build();

        // no client or vendor ids - accepted
        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE,
                new AddiCommand(validItinerary, new HashSet<>(), new HashSet<>()));

        Set<Index> expectedClientIndices = new HashSet<>();
        expectedClientIndices.add(INDEX_FIRST);
        expectedClientIndices.add(INDEX_SECOND);

        Itinerary clientOnlyItinerary = new ItineraryBuilder().build();
        // client ids only - accepted
        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE + ITINERARY_CLIENT_IDS_DESC,
                new AddiCommand(clientOnlyItinerary, expectedClientIndices, new HashSet<>()));

        // vendor ids only - accepted
        Set<Index> expectedVendorIndices = new HashSet<>();
        expectedVendorIndices.add(INDEX_THIRD);
        Itinerary vendorOnlyItinerary = new ItineraryBuilder().build();

        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE + ITINERARY_VENDOR_IDS_DESC,
                new AddiCommand(vendorOnlyItinerary, new HashSet<>(), expectedVendorIndices));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddiCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_ITINERARY_NAME_BALI + ITINERARY_DEST_DESC_BALI
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                expectedMessage);

        // missing dest prefix
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + VALID_ITINERARY_DEST_BALI
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                expectedMessage);

        // missing start date prefix
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                        + VALID_ITINERARY_START_DATE_BALI + ITINERARY_END_DATE_DESC_BALI,
                expectedMessage);

        // missing end date prefix
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                        + ITINERARY_START_DATE_DESC_BALI + VALID_ITINERARY_END_DATE_BALI,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_ITINERARY_NAME_BALI + VALID_ITINERARY_DEST_BALI
                        + VALID_ITINERARY_START_DATE_BALI + VALID_ITINERARY_END_DATE_BALI,
                expectedMessage);

    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_ITINERARY_NAME_DESC + ITINERARY_DEST_DESC_BALI
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                ItineraryName.MESSAGE_CONSTRAINTS);

        // invalid dest
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + INVALID_ITINERARY_DEST_DESC
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                Destination.MESSAGE_CONSTRAINTS);

        // invalid start date
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                        + INVALID_ITINERARY_START_DATE_DESC + ITINERARY_END_DATE_DESC_BALI,
                DateRange.MESSAGE_CONSTRAINTS);

        // invalid end date
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                        + ITINERARY_START_DATE_DESC_BALI + INVALID_ITINERARY_END_DATE_DESC,
                DateRange.MESSAGE_CONSTRAINTS);

        // invalid index
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI
                + INVALID_INDEX_CLIENT_DESC, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI
                + INVALID_INDEX_VENDOR_DESC, ParserUtil.MESSAGE_INVALID_INDEX);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_ITINERARY_NAME_DESC + INVALID_ITINERARY_DEST_DESC
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                ItineraryName.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + ITINERARY_NAME_DESC_BALI + ITINERARY_DEST_DESC_BALI
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddiCommand.MESSAGE_USAGE));
    }


}
