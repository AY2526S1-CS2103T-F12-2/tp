package seedu.address.logic.commands.homeworkcommandtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.util.SampleDataUtil.getMonthName;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.homeworkcommands.AddHomeworkCommand;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.attendance.AttendanceIndex;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.homework.Homework;
import seedu.address.model.person.LessonTime;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.reminder.UnmodifiablePaymentReminder;

/**
 * Tests for {@link AddHomeworkCommand}.
 */
public class AddHomeworkCommandTest {
    private Name marcusName;
    private Phone marcusPhone;
    private Set<LessonTime> marcusLessonTime;
    private Person marcus;

    private Name johnName;
    private Phone johnPhone;
    private Set<LessonTime> johnLessonTime;
    private Person john;

    /**
     * Sets up common Person (Marcus, John) classes used across tests.
     * Ensures each test have independent instances of person class.
     */
    @BeforeEach
    public void setUp() {
        this.marcusName = new Name("Marcus");
        this.marcusPhone = new Phone("91326770");
        marcusLessonTime = new HashSet<>();
        marcusLessonTime.add(new LessonTime("1000 Sat"));
        this.marcus = new Person(marcusName, marcusPhone, marcusLessonTime);

        this.johnName = new Name("John");
        this.johnPhone = new Phone("99999999");
        johnLessonTime = new HashSet<>();
        johnLessonTime.add(new LessonTime("1100 Sat"));
        this.john = new Person(johnName, johnPhone, johnLessonTime);
    }

    /**
     * * Minimal {@link Model} stub for {@link AddHomeworkCommand} tests.
     *
     */
    private static class ModelStubFilteredOnly implements Model {
        private final ObservableList<Person> filtered;
        private final ObservableList<Reminder> filteredReminders;

        ModelStubFilteredOnly(Collection<Person> showedPeople) {
            this.filtered = FXCollections.observableArrayList(showedPeople);
            this.filteredReminders = FXCollections.observableArrayList();
        }

        //This is the method used by AddHomeworkCommand

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return filtered;
        }

        @Override
        public ObservableList<Reminder> getFilteredReminderList() {
            return filteredReminders;
        }

        @Override
        public void refreshReminders() {
            for (Person p : filtered) {
                int currentMonth = LocalDate.now().getMonth().getValue();
                UnmodifiablePaymentReminder paymentReminder =
                        UnmodifiablePaymentReminder.of(currentMonth, p, getMonthName(currentMonth));
                if (!filteredReminders.contains(paymentReminder)) {
                    filteredReminders.add(paymentReminder);
                }
            }
        }

        //These are methods not used by AddHomeworkCommand

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
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public AttendanceIndex getAttendanceIndex() {
            return new AttendanceIndex();
        }

        @Override
        public boolean hasPersonName(String name) {
            return false;
        }

        @Override
        public boolean hasGroup(GroupName name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void createGroup(GroupName name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteGroup(GroupName name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addToGroup(GroupName name, List<Person> members) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeFromGroup(GroupName name, List<Person> members) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Group> getGroupList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Set<GroupName> getGroupsOf(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void filterByGroup(GroupName name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasReminder(Reminder reminder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setReminder(Reminder target, Reminder editedReminder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addReminder(Reminder reminder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteReminder(Reminder target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredReminderList(Predicate<Reminder> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * Verifies that executing {@link AddHomeworkCommand} with a matching student
     * in the filtered list adds homework to list and displays success message
     */
    @Test
    public void execute_success_addhomework() throws Exception {
        Homework homework = new Homework("Math WS 3", LocalDate.parse("2026-10-23"));
        AddHomeworkCommand command = new AddHomeworkCommand(marcusName, homework);
        Model model = new ModelStubFilteredOnly(List.of(marcus));
        CommandResult result = command.execute(model);

        String expected = String.format(AddHomeworkCommand.MESSAGE_SUCCESS, marcus.getName(),
                homework.getDescription(), homework.getDeadline());

        assertEquals(expected, result.getFeedbackToUser());
        assertTrue(marcus.getHomeworkList().contains(homework));
        assertEquals(1, model.getFilteredReminderList().size());
    }

    /**
     * Verifies that when the target student is not present in the current filtered list
     * (e.g., after a prior search), {@link CommandException} is thrown with
     * {@link AddHomeworkCommand#MESSAGE_NO_PERSON_FOUND}
     */
    @Test
    public void execute_nameNotInFilteredList() {
        Model model = new ModelStubFilteredOnly(List.of(john));
        Homework homework = new Homework("Math WS 3", LocalDate.parse("2026-10-23"));
        AddHomeworkCommand command = new AddHomeworkCommand(marcusName, homework);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(AddHomeworkCommand.MESSAGE_NO_PERSON_FOUND, exception.getMessage());
    }

    /**
     * Verifies that attempting to add a homework already present in the student's
     * homework list throws a {@link CommandException} with {@link AddHomeworkCommand#MESSAGE_DUPLICATE_HOMEWORK} ,
     * and the list remains unchanged
     */
    @Test
    public void execute_duplicateHomework_throwsDuplicate() {
        Homework homework = new Homework("Reading", LocalDate.parse("2026-12-01"));
        marcus.addHomework(homework);

        Model model = new ModelStubFilteredOnly(List.of(marcus));
        AddHomeworkCommand command = new AddHomeworkCommand(marcusName, homework);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(AddHomeworkCommand.MESSAGE_DUPLICATE_HOMEWORK, exception.getMessage());

        assertTrue(marcus.getHomeworkList().contains(homework));
    }

    /**
     * Verifies that when multiple persons are present in the filtered list, only
     * the targeted student receives the new homework, and non-targeted students
     * are not modified.
     */
    @Test
    public void execute_onlyTargetGetsHomework_whenMultipleInFilteredList() throws Exception {
        Homework homework = new Homework("Physics WS", LocalDate.parse("2026-11-30"));
        Model model = new ModelStubFilteredOnly(List.of(john, marcus));

        AddHomeworkCommand command = new AddHomeworkCommand(marcusName, homework);
        CommandResult result = command.execute(model);

        String expected = String.format(AddHomeworkCommand.MESSAGE_SUCCESS,
                marcus.getName().fullName, homework.getDescription(), homework.getDeadline());
        assertEquals(expected, result.getFeedbackToUser());

        assertTrue(marcus.getHomeworkList().contains(homework));
        assertFalse(john.getHomeworkList().contains(homework), "Unrelated student should not change");
        // 2 payment due reminder but homework not due tomorrow
        assertEquals(2, model.getFilteredReminderList().size());
    }


    /**
     * When deadline is in the past, command still succeeds but appends a warning.
     */
    @Test
    public void execute_pastDeadline_appendsWarning() throws Exception {
        LocalDate past = LocalDate.now().minusDays(1);
        Homework homework = new Homework("Overdue WS", past);
        Model model = new ModelStubFilteredOnly(List.of(marcus));

        AddHomeworkCommand command = new AddHomeworkCommand(marcusName, homework);
        CommandResult result = command.execute(model);

        String expectedBase = String.format(AddHomeworkCommand.MESSAGE_SUCCESS,
                marcus.getName().fullName, homework.getDescription(), homework.getDeadline());

        assertTrue(result.getFeedbackToUser().startsWith(expectedBase),
                "Result should start with the success message");
        assertTrue(result.getFeedbackToUser().contains(AddHomeworkCommand.MESSAGE_DUE_DATE_PASSED),
                "Result should include the due-date warning");
        assertTrue(marcus.getHomeworkList().contains(homework), "Homework should still be added");
    }

    /**
     * When deadline is today or in the future, no warning is appended.
     */
    @Test
    public void execute_todayOrFutureDeadline_noWarning() throws Exception {

        Homework hwToday = new Homework("Today WS", LocalDate.now());

        Homework hwFuture = new Homework("Future WS", LocalDate.now().plusDays(7));

        Model model = new ModelStubFilteredOnly(List.of(marcus));

        CommandResult resToday = new AddHomeworkCommand(marcusName, hwToday).execute(model);
        String expectedToday = String.format(AddHomeworkCommand.MESSAGE_SUCCESS,
                marcus.getName().fullName, hwToday.getDescription(), hwToday.getDeadline());
        assertEquals(expectedToday, resToday.getFeedbackToUser());

        CommandResult resFuture = new AddHomeworkCommand(marcusName, hwFuture).execute(model);
        String expectedFuture = String.format(AddHomeworkCommand.MESSAGE_SUCCESS,
                marcus.getName().fullName, hwFuture.getDescription(), hwFuture.getDeadline());
        assertEquals(expectedFuture, resFuture.getFeedbackToUser());
    }


    /**
     * Tests for equality as specified under {@link AddHomeworkCommand}
     */
    @Test
    public void equals_various() {
        Homework homework1 = new Homework("A", LocalDate.parse("2026-10-01"));
        Homework homework2 = new Homework("B", LocalDate.parse("2026-10-02"));
        AddHomeworkCommand a1 = new AddHomeworkCommand(marcusName, homework1);
        AddHomeworkCommand a1copy = new AddHomeworkCommand(new Name("Marcus"),
                new Homework("A", LocalDate.parse("2026-10-01")));
        AddHomeworkCommand a2 = new AddHomeworkCommand(marcusName, homework2);
        AddHomeworkCommand b1 = new AddHomeworkCommand(johnName, homework1);

        assertEquals(a1, a1);
        assertEquals(a1, a1copy);
        assertNotEquals(a1, null);
        assertNotEquals(a1, "string");
        assertNotEquals(a1, a2);
        assertNotEquals(a1, b1);
    }
}
