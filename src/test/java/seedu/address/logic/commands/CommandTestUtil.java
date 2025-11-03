package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentFieldsContainsKeywordsPredicate;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.reminder.ReminderFieldsContainsKeywordsPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.EditReminderDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "98765432";
    public static final String VALID_PHONE_BOB = "87654321";
    public static final String VALID_LESSON_TIME_1 = "1000 Sat";
    public static final String VALID_LESSON_TIME_2 = "1430 Thu";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String LESSON_TIME_DESC_AMY =
            " " + PREFIX_LESSON_TIME + VALID_LESSON_TIME_1 + " " + PREFIX_LESSON_TIME + VALID_LESSON_TIME_2;
    public static final String LESSON_TIME_DESC_BOB = " " + PREFIX_LESSON_TIME + VALID_LESSON_TIME_2;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_LESSON_TIME_DESC = " " + PREFIX_LESSON_TIME + "02:00am"; // not in 24-hour format

    public static final String VALID_DUEDATE_1 = "2025-11-03 1400";
    public static final String VALID_DUEDATE_2 = "2026-11-05";
    public static final String VALID_DESCRIPTION_1 = "Benson: Mock test practice";
    public static final String VALID_DESCRIPTION_2 = "Payment overdue";

    public static final String DUEDATE_DESC_1 = " " + PREFIX_DATE + VALID_DUEDATE_1;
    public static final String DUEDATE_DESC_2 = " " + PREFIX_DATE + VALID_DUEDATE_2;
    public static final String DESCRIPTION_DESC_1 = " " + PREFIX_DESC + VALID_DESCRIPTION_1;
    public static final String DESCRIPTION_DESC_2 = " " + PREFIX_DESC + VALID_DESCRIPTION_2;

    public static final String INVALID_DUEDATE_DESC = " " + PREFIX_DATE + "20 Nov 25 10:10 am"; // incorrect format
    public static final String INVALID_DESCRIPTION_DESC = " " + PREFIX_DESC; // empty description

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";


    public static final EditCommand.EditPersonDescriptor DESC_AMY; // Amy has 2 lesson time
    public static final EditCommand.EditPersonDescriptor DESC_BOB; // Bob has 1 lesson time
    public static final EditReminderCommand.EditReminderDescriptor DESC_REMINDER_1;
    public static final EditReminderCommand.EditReminderDescriptor DESC_REMINDER_2;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withLessonTime(VALID_LESSON_TIME_1).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withLessonTime(VALID_LESSON_TIME_2).build();
        DESC_REMINDER_1 = new EditReminderDescriptorBuilder().withDueDate(VALID_DUEDATE_1)
                .withDescription(VALID_DESCRIPTION_1).build();
        DESC_REMINDER_2 = new EditReminderDescriptorBuilder().withDueDate(VALID_DUEDATE_2)
                .withDescription(VALID_DESCRIPTION_2).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person and reminder list and selected person or reminder
     * in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedPersonFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());
        List<Reminder> expectedReminderFilteredList = new ArrayList<>(actualModel.getFilteredReminderList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedPersonFilteredList, actualModel.getFilteredPersonList());
        assertEquals(expectedReminderFilteredList, actualModel.getFilteredReminderList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new StudentFieldsContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the reminder at the given {@code targetIndex} in the
     * {@code model}'s reminder list.
     */
    public static void showReminderAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredReminderList().size());

        Reminder reminder = model.getFilteredReminderList().get(targetIndex.getZeroBased());
        final String[] splitName = reminder.getDescription().toString().split("\\s+");
        model.updateFilteredReminderList(new ReminderFieldsContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredReminderList().size());
    }
}
