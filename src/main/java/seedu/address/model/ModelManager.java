package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.attendance.AttendanceIndex;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Reminder> filteredReminders;
    private final AttendanceIndex attendanceIndex = new AttendanceIndex();

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredReminders = new FilteredList<>(this.addressBook.getReminderList());
        initializePaymentReset();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    // ============ Groups (delegations to AddressBook) =============================

    @Override
    public boolean hasGroup(GroupName name) {
        return addressBook.hasGroup(name);
    }

    @Override
    public void createGroup(GroupName name) {
        if (hasGroup(name)) {
            throw new IllegalArgumentException("Duplicate group: " + name);
        }
        addressBook.addGroup(new seedu.address.model.group.Group(name));
    }

    @Override
    public void deleteGroup(GroupName name) {
        if (!hasGroup(name)) {
            throw new IllegalArgumentException("Group not found: " + name);
        }
        addressBook.removeGroup(name);
    }

    @Override
    public void addToGroup(GroupName name, List<Person> members) {
        if (!hasGroup(name)) {
            throw new IllegalArgumentException("Group not found: " + name);
        }
        addressBook.addMembers(name, members);
    }

    @Override
    public void removeFromGroup(GroupName name, List<Person> members) {
        if (!hasGroup(name)) {
            throw new IllegalArgumentException("Group not found: " + name);
        }
        addressBook.removeMembers(name, members);
    }

    @Override
    public ObservableList<Group> getGroupList() {
        return addressBook.getGroupList();
    }

    @Override
    public Set<GroupName> getGroupsOf(Person person) {
        return addressBook.getGroupsOf(person);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        attendanceIndex.clear();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        attendanceIndex.removeAllForName(target.getName().fullName);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredReminders.equals(otherModelManager.filteredReminders);
    }

    @Override
    public AttendanceIndex getAttendanceIndex() {
        return attendanceIndex;
    }

    @Override
    public boolean hasPersonName(String name) {
        String norm = name.trim().replaceAll("\\s+", " ").toLowerCase();
        return filteredPersons.stream()
                .anyMatch(p -> p.getName().fullName.trim().replaceAll("\\s+", " ").toLowerCase().equals(norm));
    }

    // ========== Reminder List ====================================================================
    @Override
    public boolean hasReminder(Reminder reminder) {
        requireNonNull(reminder);
        return addressBook.hasReminder(reminder);
    }

    @Override
    public void deleteReminder(Reminder target) {
        addressBook.removeReminder(target);
    }

    @Override
    public void addReminder(Reminder reminder) {
        addressBook.addReminder(reminder);
        updateFilteredReminderList(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void setReminder(Reminder target, Reminder editedReminder) {
        requireAllNonNull(target, editedReminder);

        addressBook.setReminder(target, editedReminder);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Reminder} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Reminder> getFilteredReminderList() {
        return filteredReminders;
    }

    @Override
    public void updateFilteredReminderList(Predicate<Reminder> predicate) {
        requireNonNull(predicate);
        filteredReminders.setPredicate(predicate);
    }

    @Override
    public void refreshReminders() {
        addressBook.refreshUnmodifiableReminders();
        updateFilteredReminderList(PREDICATE_SHOW_ALL_REMINDERS);
    }

    /**
     * Initializes payment reset for all students if it's a new year
     */
    private void initializePaymentReset() {
        for (Person person : filteredPersons) {
            person.resetPaymentsIfNewYear();
        }
    }
}
