package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PersonMatchesFieldsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (isGlobalSearch(args)) {
            List<String> keywords = Arrays.asList(args.trim().split("\\s+"));
            return new FindCommand(new PersonContainsKeywordsPredicate(keywords));
        }

        return parseFieldSearch(args);
    }

    /**
     * Parses a field-based find command and returns a {@code FindCommand}.
     * The input must not contain any preamble and must contain at least one supported prefixed field.
     *
     * @param trimmedArgs User input with leading and trailing whitespace removed.
     * @return A {@code FindCommand} containing a {@code PersonMatchesFieldsPredicate}.
     * @throws ParseException If the input contains a preamble or does not contain any searchable prefixed field.
     */
    private FindCommand parseFieldSearch(String trimmedArgs) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(trimmedArgs, PREFIX_ROLE, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = argMultimap.getAllValues(PREFIX_NAME);
        List<String> phoneKeywords = argMultimap.getAllValues(PREFIX_PHONE);
        List<String> emailKeywords = argMultimap.getAllValues(PREFIX_EMAIL);
        List<String> addressKeywords = argMultimap.getAllValues(PREFIX_ADDRESS);
        List<String> tagKeywords = argMultimap.getAllValues(PREFIX_TAG);

        nameKeywords = splitValuesIntoKeywords(nameKeywords);
        phoneKeywords = splitValuesIntoKeywords(phoneKeywords);
        emailKeywords = splitValuesIntoKeywords(emailKeywords);
        addressKeywords = splitValuesIntoKeywords(addressKeywords);
        tagKeywords = splitValuesIntoKeywords(tagKeywords);

        if (nameKeywords.isEmpty() && phoneKeywords.isEmpty() && emailKeywords.isEmpty()
                && addressKeywords.isEmpty() && tagKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonMatchesFieldsPredicate(
                nameKeywords, phoneKeywords, emailKeywords, addressKeywords, tagKeywords));
    }

    /**
     * Splits each prefixed value into whitespace-delimited keywords and discards blank tokens.
     */
    private List<String> splitValuesIntoKeywords(List<String> values) {
        return values.stream()
                .flatMap(value -> Arrays.stream(value.trim().split("\\s+")))
                .filter(token -> !token.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Returns true if the given input should be treated as a global search.
     * A global search is one that does not contain any supported search prefixes.
     *
     * @param trimmedArgs User input with leading and trailing whitespace removed.
     * @return True if the input does not contain any supported field-search prefixes, false otherwise.
     */
    public boolean isGlobalSearch(String trimmedArgs) {
        trimmedArgs = trimmedArgs.toLowerCase();
        return !(trimmedArgs.contains(" n/") || trimmedArgs.startsWith("n/")
                || trimmedArgs.contains(" p/") || trimmedArgs.startsWith("p/")
                || trimmedArgs.contains(" e/") || trimmedArgs.startsWith("e/")
                || trimmedArgs.contains(" a/") || trimmedArgs.startsWith("a/")
                || trimmedArgs.contains(" t/") || trimmedArgs.startsWith("t/"));
    }

}
