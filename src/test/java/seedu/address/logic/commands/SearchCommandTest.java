package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.StudentFieldsContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class SearchCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        StudentFieldsContainsKeywordsPredicate firstPredicate =
                new StudentFieldsContainsKeywordsPredicate(Collections.singletonList("first"));
        StudentFieldsContainsKeywordsPredicate secondPredicate =
                new StudentFieldsContainsKeywordsPredicate(Collections.singletonList("second"));

        SearchCommand searchFirstCommand = new SearchCommand(firstPredicate);
        SearchCommand searchSecondCommand = new SearchCommand(secondPredicate);

        // same object -> returns true
        assertTrue(searchFirstCommand.equals(searchFirstCommand));

        // same values -> returns true
        SearchCommand findFirstCommandCopy = new SearchCommand(firstPredicate);
        assertTrue(searchFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(searchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(searchFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(searchFirstCommand.equals(searchSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = seedu.address.logic.commands.SearchCommand.MESSAGE_NO_MATCH;
        StudentFieldsContainsKeywordsPredicate predicate = preparePredicate(" ");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage =
                String.format(SearchCommand.MESSAGE_SUCCESS_PREFIX, 3);
        StudentFieldsContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        StudentFieldsContainsKeywordsPredicate predicate =
                new StudentFieldsContainsKeywordsPredicate(Arrays.asList("keyword"));
        SearchCommand findCommand = new SearchCommand(predicate);
        String expected = SearchCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private StudentFieldsContainsKeywordsPredicate preparePredicate(String userInput) {
        return new StudentFieldsContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
