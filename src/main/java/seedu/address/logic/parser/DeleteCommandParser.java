package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentFieldsContainsKeywordsPredicate;


/**
 * Parses input arguments and creates a new DeleteCommand object
 *
 * This parser expects an index to be provided as an argument in the form:
 * {@code delete i/INDEX}, where {@code INDEX} refers to the position of the student
 * in the displayed list that should be deleted.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {
    private static final Pattern VALID_KEYWORDS = Pattern.compile("[A-Za-z0-9 ':]+");

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_KEYWORD);

        boolean hasIndex = argMultimap.getValue(PREFIX_INDEX).isPresent();
        boolean hasKeyword = argMultimap.getValue(PREFIX_KEYWORD).isPresent();

        if (hasIndex == hasKeyword) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (hasIndex) {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX);
            try {
                Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
                return new DeleteCommand(index);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_KEYWORD);
        String raw = argMultimap.getValue(PREFIX_KEYWORD).get().trim().replaceAll("\\s+", " ");
        if (raw.isEmpty() || !VALID_KEYWORDS.matcher(raw).matches()) {
            throw new ParseException("Invalid search keyword.");
        }

        List<String> tokens = Arrays.stream(raw.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        return new DeleteCommand(new StudentFieldsContainsKeywordsPredicate(tokens));
    }

    /**
     * Returns {@code true} if all specified prefixes are present in the given {@link ArgumentMultimap},
     * meaning each prefix has a non-empty {@code Optional} value.
     *
     * @param argumentMultimap the mapping of prefixes to their arguments, obtained from tokenizing user input.
     * @param prefixes the prefixes to check for presence.
     * @return {@code true} if all specified prefixes are present and non-empty; {@code false} otherwise.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
