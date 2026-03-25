package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ShowCommand;

public class ShowCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowCommand.MESSAGE_USAGE);
    private ShowCommandParser parser = new ShowCommandParser();

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, "-1", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser, "a", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validIndex_success() {
        ShowCommand expectedCommand = new ShowCommand(INDEX_FIRST);
        assertParseSuccess(parser, "1", expectedCommand);
    }
}
