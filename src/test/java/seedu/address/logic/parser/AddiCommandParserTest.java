package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddiCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.testutil.ItineraryBuilder;

public class AddiCommandParserTest {

    private AddiCommandParser parser = new AddiCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        Itinerary validItinerary = new ItineraryBuilder().build();
        assertParseSuccess(parser, " n/5D4N Trip to France dest/France from/2024-12-01 to/2024-12-05",
                            new AddiCommand(validItinerary));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {

        assertThrows(ParseException.class, () -> new AddiCommandParser().parse("n/3D2N Bali from/2025-02-02 "
                                                                                + "to/2025-02-05"));

    }


}
