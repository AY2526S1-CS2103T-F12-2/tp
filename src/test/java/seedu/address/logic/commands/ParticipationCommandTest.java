package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.attendance.AttendanceIndex;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.ParticipationRecord;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@link ParticipationCommand} under participation semantics (s/0..5).
 */
public class ParticipationCommandTest {

    @Test
    public void execute_success_scoreRecorded() throws Exception {
        ModelStubWithPerson model = new ModelStubWithPerson("Alex Yeoh");
        String cmdName = "Alex Yeoh";
        String date = "2025-09-19";
        String score = "3";

        ParticipationCommand cmd = new ParticipationCommand(cmdName, date, score);
        CommandResult result = cmd.execute(model);

        assertEquals("Success: Participation recorded: Alex Yeoh, 2025-09-19, score=3.",
                result.getFeedbackToUser());

        // Verify the most recent participation on the person
        ParticipationRecord recent = model.person.getParticipation().mostRecent();
        assertEquals(LocalDate.parse(date), recent.getDate());
        assertEquals(3, recent.getScore());
    }

    @Test
    public void execute_sameDate_replacesScore() throws Exception {
        ParticipationCommand first = new ParticipationCommand("Alex Yeoh", "2025-09-19", "3");
        ParticipationCommand second = new ParticipationCommand("Alex Yeoh", "2025-09-19", "4");
        ModelStubWithPerson model = new ModelStubWithPerson("Alex Yeoh");

        first.execute(model);
        CommandResult result = second.execute(model);

        assertEquals("Success: Participation recorded: Alex Yeoh, 2025-09-19, score=4.",
                result.getFeedbackToUser());
        assertEquals(1, model.person.getParticipation().size());
        ParticipationRecord recent = model.person.getParticipation().mostRecent();
        assertEquals(java.time.LocalDate.parse("2025-09-19"), recent.getDate());
        assertEquals(4, recent.getScore());
    }

    @Test
    public void execute_invalidName_throws() {
        Model model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Nonexistent", "2025-09-19", "3");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Invalid student name: no matching student found.", ex.getMessage());
    }

    @Test
    public void execute_invalidDate_throws() {
        Model model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", "19-09-2025", "3");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Invalid date. The format must be YYYY-MM-DD.", ex.getMessage());
    }

    @Test
    public void execute_invalidScore_throws() {
        Model model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", "2025-09-19", "9");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Invalid participation score. Must be between 0 and 5 inclusive.", ex.getMessage());
    }

    @Test
    public void execute_scoreZero_ok() throws Exception {
        ModelStubWithPerson model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", "2025-09-21", "0");

        CommandResult result = cmd.execute(model);
        assertEquals("Success: Participation recorded: Alex Yeoh, 2025-09-21, score=0.",
                result.getFeedbackToUser());

        ParticipationRecord recent = model.person.getParticipation().mostRecent();
        assertEquals(LocalDate.parse("2025-09-21"), recent.getDate());
        assertEquals(0, recent.getScore());
    }

    @Test
    public void execute_scoreFive_ok() throws Exception {
        ModelStubWithPerson model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", "2025-09-22", "5");

        CommandResult result = cmd.execute(model);
        assertEquals("Success: Participation recorded: Alex Yeoh, 2025-09-22, score=5.",
                result.getFeedbackToUser());

        ParticipationRecord recent = model.person.getParticipation().mostRecent();
        assertEquals(LocalDate.parse("2025-09-22"), recent.getDate());
        assertEquals(5, recent.getScore());
    }

    @Test
    public void execute_nonIntegerScore_throws() {
        Model model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", "2025-09-19", "abc");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Invalid participation score. Use an integer 0 to 5.", ex.getMessage());
    }

    @Test
    public void execute_nameNormalization_ok() throws Exception {
        ModelStubWithPerson model = new ModelStubWithPerson("Alex   Yeoh");
        String rawName = "  alex   YEoh  ";
        String expectedName = rawName.trim().replaceAll("\\s+", " ");

        ParticipationCommand cmd = new ParticipationCommand(rawName, "2025-09-23", "3");
        CommandResult result = cmd.execute(model);

        assertEquals("Success: Participation recorded: " + expectedName + ", 2025-09-23, score=3.",
                result.getFeedbackToUser());

        ParticipationRecord recent = model.person.getParticipation().mostRecent();
        assertEquals(LocalDate.parse("2025-09-23"), recent.getDate());
        assertEquals(3, recent.getScore());
    }

    @Test
    public void execute_setsUiDate_currentDateUpdated() throws Exception {
        ModelStubWithPerson model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", "2025-09-24", "2");

        cmd.execute(model);
        assertEquals(LocalDate.parse("2025-09-24"), model.getAttendanceIndex().getCurrentUiDate());
    }

    @Test
    public void execute_historyCapsAtFive_oldestDropped() throws Exception {
        ModelStubWithPerson model = new ModelStubWithPerson("Alex Yeoh");

        // Add six consecutive classes -> only the last five remain in history
        new ParticipationCommand("Alex Yeoh", "2025-09-10", "1").execute(model);
        new ParticipationCommand("Alex Yeoh", "2025-09-11", "2").execute(model);
        new ParticipationCommand("Alex Yeoh", "2025-09-12", "3").execute(model);
        new ParticipationCommand("Alex Yeoh", "2025-09-13", "4").execute(model);
        new ParticipationCommand("Alex Yeoh", "2025-09-14", "5").execute(model);
        new ParticipationCommand("Alex Yeoh", "2025-09-15", "1").execute(model);

        List<ParticipationRecord> five = model.person.getParticipation().asListPaddedToFive();
        // Should be exactly five, oldest -> newest, with the *original* oldest dropped
        assertEquals(5, five.size());
        assertEquals(LocalDate.parse("2025-09-11"), five.get(0).getDate());
        assertEquals(LocalDate.parse("2025-09-15"), five.get(4).getDate());
    }

    @Test
    public void execute_invalidLongName_throws() {
        String over50 = "A".repeat(51);
        Model model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand(over50, "2025-09-19", "3");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Invalid student name: A name that is longer than 50 characters.", ex.getMessage());
    }

    @Test
    public void execute_emptyName_throws() {
        Model model = new ModelStubWithPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("   \t  ", "2025-09-19", "3");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Invalid student name: name cannot be empty.", ex.getMessage());
    }

    @Test
    public void execute_futureDate_throws() {
        Model model = new ParticipationCommandTest.ModelStubWithPerson("Alex Yeoh");
        String future = LocalDate.now().plusDays(1).toString();
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", future, "3");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Invalid date. Participation date cannot be in the future.", ex.getMessage());
    }

    @Test
    public void execute_today_ok() throws Exception {
        ModelStubWithPerson model = new ParticipationCommandTest.ModelStubWithPerson("Alex Yeoh");
        String today = LocalDate.now().toString();
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", today, "2");

        CommandResult result = cmd.execute(model);
        assertEquals("Success: Participation recorded: Alex Yeoh, " + today + ", score=2.",
                result.getFeedbackToUser());

        ParticipationRecord recent = model.person.getParticipation().mostRecent();
        assertEquals(LocalDate.parse(today), recent.getDate());
        assertEquals(2, recent.getScore());
    }

    /**
     * Minimal model stub supporting hasPersonName() and AttendanceIndex.
     * Model stub that:
     * - says a single person name exists via hasPersonName(...)
     * - exposes a tiny ReadOnlyAddressBook containing that Person
     * - provides an AttendanceIndex for the command to update UI date
     */
    static class ModelStubWithPerson implements Model {
        final Person person;
        private final AttendanceIndex index = new AttendanceIndex();

        ModelStubWithPerson(String storedName) {
            // Build a valid Person using the test builder defaults, overriding only the name
            this.person = new PersonBuilder().withName(storedName).build();
        }

        private static String norm(String s) {
            return s.trim().replaceAll("\\s+", " ").toLowerCase();
        }

        // --- Methods used by AttendanceCommand ---

        @Override
        public boolean hasPersonName(String name) {
            return norm(name).equals(norm(person.getName().fullName));
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            // Provide a tiny read-only view exposing just our person
            return new ReadOnlyAddressBook() {
                private final ObservableList<Person> list =
                        FXCollections.observableArrayList(person);

                @Override
                public ObservableList<Person> getPersonList() {
                    return list;
                }

                @Override
                public ObservableList<Group> getGroups() {
                    return FXCollections.observableArrayList();
                }

                @Override
                public ObservableList<Reminder> getReminderList() {
                    return null;
                }
            };
        }

        @Override
        public AttendanceIndex getAttendanceIndex() {
            return index;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            // no-op for tests
        }

        @Override
        public void updateFilteredReminderList(Predicate<Reminder> predicate) {
            // no-op
        }

        // --- Unused methods (throw AssertionError if called) ---

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError();
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return new UserPrefs();
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError();
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError();
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError();
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new AssertionError();
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError();
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError();
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError();
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError();
        }

        @Override
        public boolean hasReminder(Reminder reminder) {
            throw new AssertionError();
        }

        @Override
        public void deleteReminder(Reminder target) {
            throw new AssertionError();
        }

        @Override
        public void addReminder(Reminder reminder) {
            throw new AssertionError();
        }

        @Override
        public void setReminder(Reminder target, Reminder editedReminder) {
            throw new AssertionError();
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Reminder> getFilteredReminderList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void refreshReminders() {
            throw new AssertionError("This method should not be called.");
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
}
