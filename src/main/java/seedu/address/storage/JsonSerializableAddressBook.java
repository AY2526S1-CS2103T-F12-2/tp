package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {
    public static final String MESSAGE_DUPLICATE_PERSON = "Students list contains duplicate student(s).";
    public static final String MESSAGE_DUPLICATE_GROUP = "Groups list contains duplicate group(s).";
    public static final String MESSAGE_DUPLICATE_REMINDER = "Reminders list contains duplicate reminder(s).";
    public static final String MESSAGE_UNKNOWN_MEMBER = "Group '%s' refers to unknown member '%s'.";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();
    private final List<JsonAdaptedReminder> reminders = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and reminders.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                        @JsonProperty("groups") List<JsonAdaptedGroup> groups,
                                       @JsonProperty("reminders") List<JsonAdaptedReminder> reminders) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (groups != null) {
            this.groups.addAll(groups);
        }
        if (reminders != null) {
            this.reminders.addAll(reminders);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).toList());
        this.groups.addAll(source.getGroups().stream().map(JsonAdaptedGroup::new).toList());
        reminders.addAll(source.getReminderList().stream().map(JsonAdaptedReminder::new).toList());
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }

        for (JsonAdaptedGroup jsonAdaptedGroup : groups) {
            Group group = jsonAdaptedGroup.toModelType();
            if (addressBook.hasGroup(group.getName())) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_GROUP);
            }
            addressBook.addGroup(group);
        }

        for (JsonAdaptedGroup jag : groups) {
            GroupName gName = GroupName.of(jag.getName());
            List<Person> resolved = new ArrayList<>();
            for (String memberName : jag.getMemberNames()) {
                Person p = findByFullName(addressBook, memberName);
                if (p == null) {
                    throw new IllegalValueException(String.format(MESSAGE_UNKNOWN_MEMBER, gName, memberName));
                }
                resolved.add(p);
            }
            if (!resolved.isEmpty()) {
                addressBook.addMembers(gName, resolved);
            }
        }

        for (JsonAdaptedReminder jsonAdaptedReminder : reminders) {
            Reminder reminder = jsonAdaptedReminder.toModelType();
            if (addressBook.hasReminder(reminder)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_REMINDER);
            }
            addressBook.addReminder(reminder);
        }
        addressBook.sortReminder();

        return addressBook;
    }

    /** Helper: find person by exact full name (case-insensitive, trimmed). */
    private static Person findByFullName(AddressBook ab, String name) {
        if (name == null) {
            return null;
        }
        String needle = name.trim().toLowerCase();
        return ab.getPersonList().stream()
                .filter(p -> p.getName().fullName.trim().toLowerCase().equals(needle))
                .findFirst()
                .orElse(null);
    }

}
