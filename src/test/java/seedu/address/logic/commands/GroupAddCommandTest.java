package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.attendance.AttendanceIndex;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;
import seedu.address.testutil.PersonBuilder;

public class GroupAddCommandTest {

    @Test
    public void execute_success_addsMembers() throws Exception {
        ModelStubAccepting model = new ModelStubAccepting();
        GroupName g = GroupName.of("Group A");
        model.createGroup(g);

        GroupAddCommand cmd = new GroupAddCommand(
                g,
                List.of(Index.fromOneBased(1), Index.fromOneBased(3))
        );

        CommandResult result = cmd.execute(model);

        assertEquals(
                String.format(GroupAddCommand.MESSAGE_ADDED_FMT, 2, g),
                result.getFeedbackToUser()
        );
        // Verify membership recorded via our stub (0-based positions 0 and 2)
        assertEquals(Set.of(0, 2), model.getGroupMembersIndexZero());
    }

    @Test
    public void execute_groupMissing_throws() {
        ModelStubAccepting model = new ModelStubAccepting();
        GroupAddCommand cmd = new GroupAddCommand(
                GroupName.of("Nope"),
                List.of(Index.fromOneBased(1))
        );
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_indexOutOfBounds_reportsMessage() throws Exception {
        ModelStubAccepting model = new ModelStubAccepting();
        GroupName g = GroupName.of("Group A");
        model.createGroup(g);

        GroupAddCommand cmd = new GroupAddCommand(
                g,
                List.of(Index.fromOneBased(5)) // out of range (only 3 people)
        );

        CommandResult result = cmd.execute(model);

        String expected = String.join("\n",
                String.format(GroupAddCommand.MESSAGE_NO_CHANGES_FMT, g),
                String.format(GroupAddCommand.MESSAGE_INVALID_INDICES_FMT, "i/5")
        );

        assertEquals(expected, result.getFeedbackToUser());
        assertEquals(Collections.emptySet(), model.getGroupMembersIndexZero());
    }

    @Test
    public void execute_duplicatesReported_reportsMessage() throws Exception {
        ModelStubAccepting model = new ModelStubAccepting();
        GroupName g = GroupName.of("Dupes");
        model.createGroup(g);

        // Pre-add #2 (Bernice) so index 2 is already in the group
        new GroupAddCommand(g, List.of(Index.fromOneBased(2))).execute(model);
        assertEquals(Set.of(1), model.getGroupMembersIndexZero());

        // Try to add [2, 2, 3]; second i/2 is a duplicate in the same command
        GroupAddCommand cmd = new GroupAddCommand(
                g,
                List.of(Index.fromOneBased(2), Index.fromOneBased(2), Index.fromOneBased(3))
        );

        CommandResult result = cmd.execute(model);

        String expected = String.join("\n",
                String.format(GroupAddCommand.MESSAGE_ADDED_FMT, 1, g),
                String.format(GroupAddCommand.MESSAGE_SKIPPED_DUPLICATE_INDICES_FMT, "i/2"),
                String.format(GroupAddCommand.MESSAGE_ALREADY_IN_GROUP_FMT, "Bernice Yu")
        );

        assertEquals(expected, result.getFeedbackToUser());
        assertEquals(Set.of(1, 2), model.getGroupMembersIndexZero());
    }


    @Test
    public void execute_alreadyMembers_noChanges() throws Exception {
        ModelStubAccepting model = new ModelStubAccepting();
        GroupName g = GroupName.of("Existing");
        model.createGroup(g);

        // Pre-add #1 (Alex)
        new GroupAddCommand(g, List.of(Index.fromOneBased(1))).execute(model);
        assertEquals(Set.of(0), model.getGroupMembersIndexZero());

        // Try to add #1 again
        GroupAddCommand cmd = new GroupAddCommand(
                g,
                List.of(Index.fromOneBased(1))
        );

        CommandResult result = cmd.execute(model);

        String expected = String.join("\n",
                String.format(GroupAddCommand.MESSAGE_NO_CHANGES_FMT, g),
                String.format(GroupAddCommand.MESSAGE_ALREADY_IN_GROUP_FMT, "Alex Yeoh")
        );

        assertEquals(expected, result.getFeedbackToUser());
        assertEquals(Set.of(0), model.getGroupMembersIndexZero());
    }

    @Test
    public void execute_mixed_reportsAll() throws Exception {
        ModelStubAccepting model = new ModelStubAccepting();
        GroupName g = GroupName.of("Mixed");
        model.createGroup(g);

        // Pre-add #2 (Bernice)
        new GroupAddCommand(g, List.of(Index.fromOneBased(2))).execute(model);
        assertEquals(Set.of(1), model.getGroupMembersIndexZero());

        // Attempt to add: [2 (already), 2 (dup), 3 (new), 9 (invalid)]
        GroupAddCommand cmd = new GroupAddCommand(
                g,
                List.of(
                        Index.fromOneBased(2),
                        Index.fromOneBased(2),
                        Index.fromOneBased(3),
                        Index.fromOneBased(9))
        );

        CommandResult result = cmd.execute(model);

        String expected = String.join("\n",
                String.format(GroupAddCommand.MESSAGE_ADDED_FMT, 1, g),
                String.format(GroupAddCommand.MESSAGE_SKIPPED_DUPLICATE_INDICES_FMT, "i/2"),
                String.format(GroupAddCommand.MESSAGE_ALREADY_IN_GROUP_FMT, "Bernice Yu"),
                String.format(GroupAddCommand.MESSAGE_INVALID_INDICES_FMT, "i/9")
        );

        assertEquals(expected, result.getFeedbackToUser());
        assertEquals(Set.of(1, 2), model.getGroupMembersIndexZero());
    }

    /**
     * Minimal model stub that accepts group operations and holds a small person list.
     */
    private static class ModelStubAccepting implements Model {

        private final List<Person> persons = List.of(
                new PersonBuilder().withName("Alex Yeoh").build(),
                new PersonBuilder().withName("Bernice Yu").build(),
                new PersonBuilder().withName("Charlotte Oliveiro").build()
        );

        private final Set<GroupName> groups = new java.util.HashSet<>();
        private final java.util.Map<GroupName, java.util.Set<Integer>> membership = new java.util.HashMap<>();
        private Set<Integer> groupMembersIndexZero = Collections.emptySet();

        private Set<Integer> setCopy(java.util.Set<Integer> set) {
            return Set.copyOf(set);
        }

        private java.util.Set<Integer> ensureBucket(GroupName name) {
            return membership.computeIfAbsent(name, k -> new java.util.HashSet<>());
        }

        // ----- Accessor for assertion -----
        public Set<Integer> getGroupMembersIndexZero() {
            return groupMembersIndexZero;
        }

        // ----- Groups API -----
        @Override
        public boolean hasGroup(GroupName name) {
            return groups.contains(name);
        }

        @Override
        public void createGroup(GroupName name) {
            groups.add(name);
            membership.put(name, new java.util.HashSet<>());
        }

        @Override
        public void deleteGroup(GroupName name) {
            groups.remove(name);
            membership.remove(name);
        }

        @Override
        public void addToGroup(GroupName name, List<Person> members) {
            java.util.Set<Integer> set = ensureBucket(name);
            for (Person p : members) {
                int idx = persons.indexOf(p);
                if (idx >= 0) {
                    set.add(idx);
                }
            }
            groupMembersIndexZero = setCopy(set);
        }

        @Override
        public void removeFromGroup(GroupName name, List<Person> members) {
            java.util.Set<Integer> set = ensureBucket(name);
            for (Person p : members) {
                int idx = persons.indexOf(p);
                if (idx >= 0) {
                    set.remove(idx);
                }
            }
            groupMembersIndexZero = setCopy(set);
        }

        @Override
        public ObservableList<Group> getGroupList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public Set<GroupName> getGroupsOf(Person person) {
            int idx = persons.indexOf(person);
            if (idx < 0) {
                return Set.of();
            }
            Set<GroupName> res = new HashSet<>();
            for (var e : membership.entrySet()) {
                if (e.getValue().contains(idx)) {
                    res.add(e.getKey());
                }
            }
            return res;
        }

        // ----- Person list plumbing used by command -----
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(persons);
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            // not needed for this test
        }

        @Override
        public void updateFilteredReminderList(Predicate<Reminder> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        // ----- Attendance/lookup methods required by Model -----
        @Override
        public boolean hasPersonName(String name) {
            if (name == null) {
                return false;
            }
            String needle = name.trim().toLowerCase();
            return persons.stream()
                    .anyMatch(p -> p.getName().fullName.trim().toLowerCase().equals(needle));
        }

        @Override
        public AttendanceIndex getAttendanceIndex() {
            return new AttendanceIndex();
        }

        // ----- Unused Model methods (expanded to satisfy LeftCurly) -----
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
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
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public boolean hasPerson(Person person) {
            return persons.contains(person);
        }

        @Override
        public void deletePerson(Person target) {
        }

        @Override
        public void addPerson(Person person) {
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
        }

        @Override
        public void addReminder(Reminder reminder) {
            throw new AssertionError("This method should not be called.");
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
        public ObservableList<Reminder> getFilteredReminderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void refreshReminders() {
            throw new AssertionError("This method should not be called.");
        }
    }
}
