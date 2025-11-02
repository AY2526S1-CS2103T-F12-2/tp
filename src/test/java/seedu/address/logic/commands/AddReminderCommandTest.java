package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUEDATE_1;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalReminders.REMINDER_1;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.attendance.AttendanceIndex;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;
import seedu.address.testutil.ReminderBuilder;

public class AddReminderCommandTest {
    @Test
    public void constructor_nullReminder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddReminderCommand(null));
    }

    @Test
    public void execute_reminderAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingReminderAdded modelStub = new ModelStubAcceptingReminderAdded();
        Reminder validReminder = new ReminderBuilder().build();

        CommandResult commandResult = new AddReminderCommand(validReminder).execute(modelStub);

        assertEquals(String.format(AddReminderCommand.MESSAGE_SUCCESS, Messages.format(validReminder)),
                commandResult.getFeedbackToUser());
        assertEquals(List.of(validReminder), modelStub.remindersAdded);
    }

    @Test
    public void execute_duplicateReminder_throwsCommandException() {
        Reminder validReminder = new ReminderBuilder().build();
        AddReminderCommand addCommand = new AddReminderCommand(validReminder);
        ModelStub modelStub = new ModelStubWithReminder(validReminder);

        assertThrows(CommandException.class,
                AddReminderCommand.MESSAGE_DUPLICATE_REMINDER, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_addOverdueReminder_throwsCommandException() {
        ModelStubAcceptingReminderAdded modelStub = new ModelStubAcceptingReminderAdded();
        Reminder reminder = new ReminderBuilder().withDueDate("2020-11-11").build();
        AddReminderCommand addCommand = new AddReminderCommand(reminder);
        assertThrows(CommandException.class, AddReminderCommand.MESSAGE_OVERDUE_REMINDER, ()
                -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Reminder r1 = new ReminderBuilder().build();
        Reminder r2 = new ReminderBuilder().withDueDate(VALID_DUEDATE_1).build();
        AddReminderCommand addR1Command = new AddReminderCommand(r1);
        AddReminderCommand addR2Command = new AddReminderCommand(r2);

        // same object -> returns true
        assertEquals(addR1Command, addR1Command);

        // same values -> returns true
        AddReminderCommand addR1CommandCopy = new AddReminderCommand(r1);
        assertEquals(addR1Command, addR1CommandCopy);

        // different types -> returns false
        assertNotEquals(1, addR1Command);

        // null -> returns false
        assertNotEquals(null, addR1Command);

        // different reminder -> returns false
        assertNotEquals(addR1Command, addR2Command);
    }

    @Test
    public void toStringMethod() {
        AddReminderCommand addCommand = new AddReminderCommand(REMINDER_1);
        String expected = AddReminderCommand.class.getCanonicalName() + "{toAdd=" + REMINDER_1 + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addReminder(Reminder reminder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPersonName(String person) {
            throw new AssertionError("This method shout not be called");
        }

        @Override
        public boolean hasReminder(Reminder reminder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteReminder(Reminder target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setReminder(Reminder target, Reminder editedReminder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Reminder> getFilteredReminderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredReminderList(Predicate<Reminder> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void refreshReminders() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public AttendanceIndex getAttendanceIndex() {
            return new AttendanceIndex(); // harmless default for existing add tests
        }

        // ===== Groups (no-op implementations for tests) =====
        @Override
        public boolean hasGroup(GroupName name) {
            return false;
        }

        @Override
        public void createGroup(GroupName name) {
            // no-op for tests
        }

        @Override
        public void deleteGroup(GroupName name) {
            // no-op for tests
        }

        @Override
        public void addToGroup(GroupName name, List<Person> members) {
            // no-op for tests
        }

        @Override
        public void removeFromGroup(GroupName name, List<Person> members) {
            // no-op for tests
        }

        @Override
        public ObservableList<Group> getGroupList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public Set<GroupName> getGroupsOf(Person person) {
            return Collections.emptySet();
        }
    }

    /**
     * A Model stub that contains a single reminder.
     */
    private class ModelStubWithReminder extends ModelStub {
        private final Reminder reminder;

        ModelStubWithReminder(Reminder reminder) {
            requireNonNull(reminder);
            this.reminder = reminder;
        }

        @Override
        public boolean hasReminder(Reminder reminder) {
            requireNonNull(reminder);
            return this.reminder.equals(reminder);
        }
    }

    /**
     * A Model stub that always accept the reminder being added.
     */
    private class ModelStubAcceptingReminderAdded extends ModelStub {
        final ArrayList<Reminder> remindersAdded = new ArrayList<>();

        @Override
        public boolean hasReminder(Reminder reminder) {
            requireNonNull(reminder);
            return remindersAdded.stream().anyMatch(reminder::equals);
        }

        @Override
        public void addReminder(Reminder reminder) {
            requireNonNull(reminder);
            remindersAdded.add(reminder);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
