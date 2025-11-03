// src/test/java/seedu/address/logic/commands/ParticipationCommandPersistenceTest.java
package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class ParticipationCommandPersistenceTest {

    /**
     * Model stub that records whether setPerson(...) was invoked.
     */
    static class ModelStubTrackingSetPerson implements Model {
        private final Person person;
        private final AttendanceIndex index = new AttendanceIndex();
        private boolean setPersonCalled = false;

        ModelStubTrackingSetPerson(String name) {
            this.person = new PersonBuilder().withName(name).build();
        }

        boolean isSetPersonCalled() {
            return setPersonCalled;
        }

        private static String norm(String s) {
            return s.trim().replaceAll("\\s+", " ").toLowerCase();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new ReadOnlyAddressBook() {
                private final ObservableList<Person> list =
                        FXCollections.observableArrayList(person);

                @Override
                public ObservableList<Person> getPersonList() {
                    return list;
                }

                @Override
                public ObservableList<Group> getGroups() {
                    return FXCollections.observableArrayList(); // no groups needed for this test
                }

                @Override
                public ObservableList<Reminder> getReminderList() {
                    return FXCollections.observableArrayList();
                }
            };
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            // no-op for test
        }

        @Override
        public AttendanceIndex getAttendanceIndex() {
            return index;
        }

        @Override
        public boolean hasPersonName(String name) {
            return norm(name).equals(norm(person.getName().fullName));
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            setPersonCalled = true;
        }

        // ===== Unused below (minimal stubs / defaults) =====
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) { }
        @Override public void refreshReminders() {}
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            return new UserPrefs();
        }
        @Override public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }
        @Override public void setGuiSettings(GuiSettings guiSettings) { }
        @Override public Path getAddressBookFilePath() {
            return null;
        }
        @Override public void setAddressBookFilePath(Path addressBookFilePath) { }
        @Override public void setAddressBook(ReadOnlyAddressBook addressBook) { }
        @Override public boolean hasPerson(Person person) {
            return false;
        }
        @Override public void deletePerson(Person target) { }
        @Override public void addPerson(Person person) { }
        @Override public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }
        @Override public boolean hasReminder(Reminder reminder) {
            return false;
        }
        @Override public void deleteReminder(Reminder target) { }
        @Override public void addReminder(Reminder reminder) { }
        @Override public void setReminder(Reminder target, Reminder editedReminder) { }
        @Override public ObservableList<Reminder> getFilteredReminderList() {
            return FXCollections.observableArrayList();
        }
        @Override public void updateFilteredReminderList(Predicate<Reminder> predicate) { }
        @Override public boolean hasGroup(GroupName name) {
            return false;
        }
        @Override public void createGroup(GroupName name) { }
        @Override public void deleteGroup(GroupName name) { }
        @Override public void addToGroup(GroupName name, List<Person> members) { }
        @Override public void removeFromGroup(GroupName name, List<Person> members) { }
        @Override public ObservableList<Group> getGroupList() {
            return FXCollections.observableArrayList();
        }
        @Override public Set<GroupName> getGroupsOf(Person person) {
            return Collections.emptySet();
        }
    }

    @Test
    public void execute_triggersSetPerson_andAddsRecord() throws Exception {
        ModelStubTrackingSetPerson model = new ModelStubTrackingSetPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("Alex Yeoh", "2025-09-19", "4");

        CommandResult result = cmd.execute(model);

        // setPerson called → persistence trigger covered
        assertTrue(
                model.isSetPersonCalled(),
                "setPerson(...) should be called to trigger persistence"
        );

        // data actually added
        ParticipationRecord recent = model.person.getParticipation().mostRecent();
        assertEquals(LocalDate.parse("2025-09-19"), recent.getDate());
        assertEquals(4, recent.getScore());

        // UI message intact
        assertEquals(
                "Success: Participation recorded: Alex Yeoh, 2025-09-19, score=4.",
                result.getFeedbackToUser()
        );
    }

    @Test
    public void execute_emptyName_throws() {
        ModelStubTrackingSetPerson model = new ModelStubTrackingSetPerson("Alex Yeoh");
        ParticipationCommand cmd = new ParticipationCommand("   ", "2025-09-19", "3");
        try {
            cmd.execute(model);
        } catch (CommandException e) {
            assertEquals("Invalid student name: name cannot be empty.", e.getMessage());
        }
    }
}
