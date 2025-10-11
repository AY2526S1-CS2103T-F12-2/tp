package seedu.address.logic.parser;


import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentFieldsContainsKeywordsPredicate;

/**
 * Parses the given {@code String} of arguments in the context of the SearchCommand
 * and returns a SearchCommand object for execution.
 *
 * @throws ParseException if the user input does not follow the expected format
 */
public class SearchCommandParser implements Parser<SearchCommand> {
    //allow letters, digits, and spaces only
    private static final Pattern VALID_CHARS = Pattern.compile("[A-Za-z0-9 ]+");

    @Override
    public SearchCommand parse(String args) throws ParseException {
        String trimmed = args.trim();

        // must start with k/
        if (!trimmed.startsWith(PREFIX_KEYWORD.getPrefix()) || trimmed.length() <= 2) {
            throw new ParseException("Invalid search keyword.");
        }

        //take value after k then trim space
        String raw = trimmed.substring(2).trim().replaceAll("\\s+", " ");
        if (raw.isEmpty() || !VALID_CHARS.matcher(raw).matches()) {
            throw new ParseException("Invalid search keyword.");
        }

        //split into list tokens for predicate
        List<String> tokens = Arrays.stream(raw.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        return new SearchCommand(new StudentFieldsContainsKeywordsPredicate(tokens));
    }
}
