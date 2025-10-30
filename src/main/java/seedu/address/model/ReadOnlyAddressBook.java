package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {
    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the groups list.
     * This list will not contain any duplicate groups.
     */
    ObservableList<Group> getGroups();

    /**
     * Returns an unmodifiable view of the reminder list.
     * This list will not contain any duplicate reminders.
     */
    ObservableList<Reminder> getReminderList();
}
