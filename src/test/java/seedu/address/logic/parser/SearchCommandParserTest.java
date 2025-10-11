package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.person.StudentFieldsContainsKeywordsPredicate;

public class SearchCommandParserTest {
    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // No k/ prefix
        assertParseFailure(parser, "     ", "Invalid search keyword.");

        // k/ but empty after normalization
        assertParseFailure(parser, PREFIX_KEYWORD + "    ", "Invalid search keyword.");
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        SearchCommand expectedSearchCommand =
                new SearchCommand(new StudentFieldsContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, PREFIX_KEYWORD + "Alice Bob", expectedSearchCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n " + PREFIX_KEYWORD + " Alice \n \t Bob  \t", expectedSearchCommand);
    }
}
