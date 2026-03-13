package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddiCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.itinerary.Itinerary;

public class AddiCommandParserTest {

    private AddiCommandParser parser = new AddiCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        Itinerary validItinerary = new Itinerary("3D2N Bali", "Bali",
                LocalDate.of(2025, 2, 2),
                LocalDate.of(2025, 2, 5));

        assertParseSuccess(parser, " n/3D2N Bali dest/Bali from/2025-02-02 to/2025-02-05",
                            new AddiCommand(validItinerary));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {

        assertThrows(ParseException.class, () -> new AddiCommandParser().parse("n/3D2N Bali from/2025-02-02 "
                                                                                + "to/2025-02-05"));

    }


}
