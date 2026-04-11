package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_DEST_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_END_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ITINERARY_START_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_DEST_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_END_DATE_DESC_BALI;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_END_DATE_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_NAME_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_START_DATE_DESC_BALI;
import static seedu.address.logic.commands.CommandTestUtil.ITINERARY_START_DATE_DESC_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_DEST_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_END_DATE_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_START_DATE_FRANCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.CONTACT_FLAG;
import static seedu.address.logic.parser.CliSyntax.ITINERARY_FLAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_DESTINATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_START;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditItineraryCommand;
import seedu.address.logic.commands.EditItineraryCommand.EditItineraryDescriptor;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.model.itinerary.DateRange;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.ItineraryName;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditItineraryDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private final EditCommandParser parser = new EditCommandParser();

    // person tests

    @Test
    public void parse_personMissingParts_failure() {
        // no index specified
        assertParseFailure(parser, CONTACT_FLAG + " " + VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no flag specified
        assertParseFailure(parser, "1" + VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, CONTACT_FLAG + " 1", EditCommand.MESSAGE_NOT_EDITED);

        // no index, no flag and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_personInvalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, CONTACT_FLAG + "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, CONTACT_FLAG + "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, CONTACT_FLAG + "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, CONTACT_FLAG + "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_personInvalidValue_failure() {
        // invalid name
        assertParseFailure(parser, CONTACT_FLAG + " 1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        // invalid phone
        assertParseFailure(parser, CONTACT_FLAG + " 1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        // invalid email
        assertParseFailure(parser, CONTACT_FLAG + " 1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);
        // invalid address
        assertParseFailure(parser, CONTACT_FLAG + " 1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);
        // invalid tag
        assertParseFailure(parser, CONTACT_FLAG + " 1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // invalid phone followed by valid email
        assertParseFailure(parser, CONTACT_FLAG + " 1"
                + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Person} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, CONTACT_FLAG + " 1"
                + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, CONTACT_FLAG + " 1"
                + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, CONTACT_FLAG + " 1"
                + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, CONTACT_FLAG + " 1"
                        + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_personAllFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND;
        String userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + PHONE_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + TAG_DESC_FRIEND;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_personSomeFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST;
        String userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_personOneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD;
        String userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_personMultipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_personrepeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST;
        String userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + TAG_DESC_FRIEND + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        // multiple invalid values
        userInput = CONTACT_FLAG + " " + targetIndex.getOneBased()
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }

    @Test
    public void parse_personResetTags_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = CONTACT_FLAG + " " + targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // itinerary tests

    @Test
    public void parse_itineraryMissingParts_failure() {
        // no index specified
        assertParseFailure(parser, ITINERARY_FLAG + " " + VALID_ITINERARY_NAME_FRANCE,
                MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, ITINERARY_FLAG + " 1",
                EditCommand.MESSAGE_NOT_EDITED);

        // no index, no flag and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_itineraryInvalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, ITINERARY_FLAG + " -5" + ITINERARY_NAME_DESC_FRANCE, MESSAGE_INVALID_INDEX);

        // zero index
        assertParseFailure(parser, ITINERARY_FLAG + " 0" + ITINERARY_NAME_DESC_FRANCE, MESSAGE_INVALID_INDEX);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, ITINERARY_FLAG + " 1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, ITINERARY_FLAG + " 1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_itineraryInvalidValue_failure() {
        // invalid itinerary name
        assertParseFailure(parser, ITINERARY_FLAG + " 1"
                + INVALID_ITINERARY_NAME_DESC, ItineraryName.MESSAGE_CONSTRAINTS);

        // invalid destination
        assertParseFailure(parser, ITINERARY_FLAG + " 1"
                + INVALID_ITINERARY_DEST_DESC, Destination.MESSAGE_CONSTRAINTS);

        // invalid start date
        assertParseFailure(parser, ITINERARY_FLAG + " 1"
                + INVALID_ITINERARY_START_DATE_DESC, DateRange.MESSAGE_INVALID_DATE_FORMAT);

        // invalid end date
        assertParseFailure(parser, ITINERARY_FLAG + " 1"
                + INVALID_ITINERARY_END_DATE_DESC, DateRange.MESSAGE_INVALID_DATE);

        // invalid destination followed by valid start date
        assertParseFailure(parser, ITINERARY_FLAG + " 1"
                + INVALID_ITINERARY_DEST_DESC + ITINERARY_START_DATE_DESC_BALI, Destination.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, ITINERARY_FLAG + " 1"
                        + INVALID_ITINERARY_NAME_DESC + INVALID_ITINERARY_DEST_DESC
                        + ITINERARY_START_DATE_DESC_BALI + ITINERARY_END_DATE_DESC_BALI,
                ItineraryName.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_itineraryAllFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND;
        String userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased() + ITINERARY_NAME_DESC_FRANCE
                + ITINERARY_DEST_DESC_FRANCE + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE;

        EditItineraryDescriptor descriptor = new EditItineraryDescriptorBuilder()
                .withName(VALID_ITINERARY_NAME_FRANCE)
                .withDestination(VALID_ITINERARY_DEST_FRANCE)
                .withStartDate(LocalDate.parse(VALID_ITINERARY_START_DATE_FRANCE))
                .withEndDate(LocalDate.parse(VALID_ITINERARY_END_DATE_FRANCE))
                .build();
        EditCommand expectedCommand = new EditItineraryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_itinerarySomeFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST;
        String userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased()
                + ITINERARY_NAME_DESC_FRANCE + ITINERARY_DEST_DESC_FRANCE;

        EditItineraryDescriptor descriptor = new EditItineraryDescriptorBuilder()
                .withName(VALID_ITINERARY_NAME_FRANCE)
                .withDestination(VALID_ITINERARY_DEST_FRANCE)
                .build();
        EditCommand expectedCommand = new EditItineraryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_itineraryOneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD;
        String userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased() + ITINERARY_NAME_DESC_FRANCE;
        EditItineraryDescriptor descriptor = new EditItineraryDescriptorBuilder()
                .withName(VALID_ITINERARY_NAME_FRANCE).build();
        EditCommand expectedCommand = new EditItineraryCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // destination
        userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased() + ITINERARY_DEST_DESC_FRANCE;
        descriptor = new EditItineraryDescriptorBuilder()
                .withDestination(VALID_ITINERARY_DEST_FRANCE).build();
        expectedCommand = new EditItineraryCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // start date
        userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased() + ITINERARY_START_DATE_DESC_FRANCE;
        descriptor = new EditItineraryDescriptorBuilder()
                .withStartDate(LocalDate.parse(VALID_ITINERARY_START_DATE_FRANCE)).build();
        expectedCommand = new EditItineraryCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // end date
        userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased() + ITINERARY_END_DATE_DESC_FRANCE;
        descriptor = new EditItineraryDescriptorBuilder()
                .withEndDate(LocalDate.parse(VALID_ITINERARY_END_DATE_FRANCE)).build();
        expectedCommand = new EditItineraryCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_itineraryMultipleRepeatedFields_failure() {

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST;
        String userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased()
                + ITINERARY_NAME_DESC_FRANCE + INVALID_ITINERARY_NAME_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ITINERARY_NAME));

        // invalid followed by valid
        userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased()
                + INVALID_ITINERARY_NAME_DESC + ITINERARY_NAME_DESC_FRANCE;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ITINERARY_NAME));

        // multiple valid fields repeated
        userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased()
                + ITINERARY_DEST_DESC_FRANCE + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE
                + ITINERARY_DEST_DESC_FRANCE + ITINERARY_START_DATE_DESC_FRANCE + ITINERARY_END_DATE_DESC_FRANCE;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_ITINERARY_DESTINATION, PREFIX_ITINERARY_START, PREFIX_ITINERARY_END));

        // multiple invalid values
        userInput = ITINERARY_FLAG + " " + targetIndex.getOneBased()
                + INVALID_ITINERARY_DEST_DESC + INVALID_ITINERARY_START_DATE_DESC + INVALID_ITINERARY_END_DATE_DESC
                + INVALID_ITINERARY_DEST_DESC + INVALID_ITINERARY_START_DATE_DESC + INVALID_ITINERARY_END_DATE_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_ITINERARY_DESTINATION, PREFIX_ITINERARY_START, PREFIX_ITINERARY_END));
    }


}
