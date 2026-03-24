package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
import static seedu.address.logic.commands.CommandTestUtil.VALID_UUID_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_UUID_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_UUID_3;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalItineraries.BALI_TRIP;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddiCommand;
import seedu.address.model.id.Id;
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
                new AddiCommand(BALI_TRIP));

        // multiple ids - accepted
        Set<Id> expectedClientIds = new HashSet<>();
        expectedClientIds.add(new Id(VALID_UUID_1));
        expectedClientIds.add(new Id(VALID_UUID_2));

        Set<Id> expectedVendorIds = new HashSet<>();
        expectedVendorIds.add(new Id(VALID_UUID_3));
        Itinerary expectedItineraryMultipleUuids = new ItineraryBuilder().withClientIds(expectedClientIds)
                .withVendorIds(expectedVendorIds).build();

        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE
                        + ITINERARY_CLIENT_IDS_DESC + ITINERARY_VENDOR_IDS_DESC,
                new AddiCommand(expectedItineraryMultipleUuids));


    }

    @Test
    public void parse_optionalFieldsMissing_success() {

        Itinerary validItinerary = new ItineraryBuilder().build();

        // no client or vendor ids - accepted
        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE,
                new AddiCommand(validItinerary));

        Set<Id> expectedClientIds = new HashSet<>();
        expectedClientIds.add(new Id(VALID_UUID_1));
        expectedClientIds.add(new Id(VALID_UUID_2));

        Itinerary clientOnlyItinerary = new ItineraryBuilder().withClientIds(expectedClientIds).build();
        // client ids only - accepted
        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE + ITINERARY_CLIENT_IDS_DESC,
                new AddiCommand(clientOnlyItinerary));

        // vendor ids only - accepted
        Set<Id> expectedVendorIds = new HashSet<>();
        expectedVendorIds.add(new Id(VALID_UUID_3));
        Itinerary vendorOnlyItinerary = new ItineraryBuilder().withVendorIds(expectedVendorIds).build();

        assertParseSuccess(parser, ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE
                        + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE + ITINERARY_VENDOR_IDS_DESC,
                new AddiCommand(vendorOnlyItinerary));

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

        // invalid id TODO
        // assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
        // + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

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
