package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_REMINDER_1;
import static seedu.address.logic.commands.CommandTestUtil.DESC_REMINDER_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_2;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showReminderAtIndex;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_REMINDER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_REMINDER;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditReminderCommand.EditReminderDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.reminder.UnmodifiablePaymentReminder;
import seedu.address.testutil.EditReminderDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.ReminderBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditReminderCommand.
 */
public class EditReminderCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        ObservableList<Reminder> list = model.getFilteredReminderList();
        Index index = Index.fromOneBased(list.size());
        Reminder editedReminder = new ReminderBuilder().build();
        EditReminderDescriptor descriptor = new EditReminderDescriptorBuilder(editedReminder).build();
        EditReminderCommand editCommand = new EditReminderCommand(index, descriptor);

        String expectedMessage = String.format(EditReminderCommand.MESSAGE_EDIT_REMINDER_SUCCESS,
                Messages.format(editedReminder));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setReminder(model.getFilteredReminderList().get(index.getZeroBased()), editedReminder);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastReminder = Index.fromOneBased(model.getFilteredReminderList().size());
        Reminder lastReminder = model.getFilteredReminderList().get(indexLastReminder.getZeroBased());

        ReminderBuilder reminderInList = new ReminderBuilder(lastReminder);
        String tmr = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        Reminder editedReminder = reminderInList.withDueDate(tmr).build();

        EditReminderDescriptor descriptor = new EditReminderDescriptorBuilder()
                .withDueDate(tmr).build();
        EditReminderCommand editCommand = new EditReminderCommand(indexLastReminder, descriptor);

        String expectedMessage = String.format(EditReminderCommand.MESSAGE_EDIT_REMINDER_SUCCESS,
                Messages.format(editedReminder));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setReminder(lastReminder, editedReminder);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditReminderCommand editCommand = new EditReminderCommand(INDEX_FIRST_REMINDER,
                new EditReminderDescriptor());
        Reminder editedReminder = model.getFilteredReminderList().get(0);

        String expectedMessage = String.format(EditReminderCommand.MESSAGE_EDIT_REMINDER_SUCCESS,
                Messages.format(editedReminder));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        ObservableList<Reminder> list = model.getFilteredReminderList();
        Index index = Index.fromOneBased(list.size());
        showReminderAtIndex(model, index);

        Reminder reminderInFilteredList = model.getFilteredReminderList().get(INDEX_FIRST_REMINDER.getZeroBased());
        Reminder editedReminder = new ReminderBuilder(reminderInFilteredList)
                .withDueDate(VALID_DUEDATE_2).build();
        EditReminderCommand editCommand = new EditReminderCommand(INDEX_FIRST_REMINDER,
                new EditReminderDescriptorBuilder().withDueDate(VALID_DUEDATE_2).build());

        String expectedMessage = String.format(EditReminderCommand.MESSAGE_EDIT_REMINDER_SUCCESS,
                Messages.format(editedReminder));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setReminder(
                model.getFilteredReminderList().get(INDEX_FIRST_REMINDER.getZeroBased()), editedReminder);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateReminderUnfilteredList_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        ObservableList<Reminder> list = model.getFilteredReminderList();
        Reminder firstReminder = model.getFilteredReminderList().get(0);
        EditReminderDescriptor descriptor = new EditReminderDescriptorBuilder(firstReminder).build();
        EditReminderCommand editCommand = new EditReminderCommand(
                Index.fromOneBased(list.size() - 1), descriptor);

        assertCommandFailure(editCommand, model, EditReminderCommand.MESSAGE_DUPLICATE_REMINDER);
    }

    @Test
    public void execute_duplicateReminderFilteredList_failure() {
        ObservableList<Reminder> list = model.getFilteredReminderList();
        Index index = Index.fromOneBased(list.size());
        showReminderAtIndex(model, index);

        // edit reminder in filtered list into a duplicate in reminder list
        Reminder reminderInList = model.getAddressBook().getReminderList().get(INDEX_FIRST_REMINDER.getZeroBased());
        EditReminderCommand editCommand = new EditReminderCommand(INDEX_FIRST_REMINDER,
                new EditReminderDescriptorBuilder(reminderInList).build());

        assertCommandFailure(editCommand, model, EditReminderCommand.MESSAGE_DUPLICATE_REMINDER);
    }

    @Test
    public void execute_invalidReminderIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredReminderList().size() + 1);
        EditReminderDescriptor descriptor = new EditReminderDescriptorBuilder().withDueDate(VALID_DUEDATE_1).build();
        EditReminderCommand editCommand = new EditReminderCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidReminderIndexFilteredList_failure() {
        ObservableList<Reminder> list = model.getFilteredReminderList();
        Index index = Index.fromOneBased(list.size());
        showReminderAtIndex(model, index);
        Index outOfBoundIndex = INDEX_SECOND_REMINDER;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getReminderList().size());

        EditReminderCommand editCommand = new EditReminderCommand(outOfBoundIndex,
                new EditReminderDescriptorBuilder().withDueDate(VALID_DUEDATE_1).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_editUnmodifiableReminder() {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        UnmodifiablePaymentReminder reminder = UnmodifiablePaymentReminder.of(6, person, "June");
        model.addReminder(reminder);

        EditReminderDescriptor descriptor = new EditReminderDescriptorBuilder().withDueDate(VALID_DUEDATE_1).build();
        EditReminderCommand command = new EditReminderCommand(INDEX_FIRST_REMINDER, descriptor);

        String expectedMessage = Messages.MESSAGE_UNMODIFIABLE_REMINDER + "\n" + reminder.getModifyMessage();
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_editedReminderIsOverdue_throwsCommandException() {
        Index indexLastReminder = Index.fromOneBased(model.getFilteredReminderList().size());
        EditReminderDescriptor descriptor = new EditReminderDescriptorBuilder()
                .withDueDate("2020-11-11").build();
        EditReminderCommand editCommand = new EditReminderCommand(indexLastReminder, descriptor);

        assertCommandFailure(editCommand, model, AddReminderCommand.MESSAGE_OVERDUE_REMINDER);
    }

    @Test
    public void equals() {
        final EditReminderCommand standardCommand = new EditReminderCommand(INDEX_FIRST_REMINDER, DESC_REMINDER_1);

        // same values -> returns true
        EditReminderDescriptor copyDescriptor = new EditReminderDescriptor(DESC_REMINDER_1);
        EditReminderCommand commandWithSameValues = new EditReminderCommand(INDEX_FIRST_REMINDER, copyDescriptor);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different index -> returns false
        assertNotEquals(new EditReminderCommand(INDEX_SECOND_REMINDER, DESC_REMINDER_1), standardCommand);

        // different descriptor -> returns false
        assertNotEquals(new EditReminderCommand(INDEX_FIRST_REMINDER, DESC_REMINDER_2), standardCommand);
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditReminderDescriptor editReminderDescriptor = new EditReminderDescriptor();
        EditReminderCommand editCommand = new EditReminderCommand(index, editReminderDescriptor);
        String expected = EditReminderCommand.class.getCanonicalName() + "{index=" + index
                + ", editReminderDescriptor=" + editReminderDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }
}
