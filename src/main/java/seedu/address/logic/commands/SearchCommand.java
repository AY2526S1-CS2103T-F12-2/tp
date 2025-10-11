package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentFieldsContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search-student";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches for students.\n"
            + "Purpose: Search for students\n"
            + "Command format: " + COMMAND_WORD + " " + PREFIX_KEYWORD + "keyword\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_KEYWORD + "marcus\n"
            + "Parameters and validation:\n"
            + "  - Keyword: letters/digits; must be ≥1 char; case-insensitive; trims/collapses spaces.\n"
            + "Error message: \"Invalid search keyword.\"";

    public static final String MESSAGE_NO_MATCH = "No students match the search keyword.";
    public static final String MESSAGE_SUCCESS_PREFIX = "Found %d students:\n";

    private final StudentFieldsContainsKeywordsPredicate predicate;

    public SearchCommand(StudentFieldsContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        List<Person> results = model.getFilteredPersonList();
        if (results.isEmpty()) {
            return new CommandResult(MESSAGE_NO_MATCH);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS_PREFIX, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SearchCommand)) {
            return false;
        }

        SearchCommand otherFindCommand = (SearchCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
