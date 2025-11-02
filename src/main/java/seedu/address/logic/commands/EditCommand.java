package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.LessonTime;
import seedu.address.model.person.Name;
import seedu.address.model.person.ParticipationRecord;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Edits the details of an existing student in the student list.
 */
public class EditCommand extends Command {
    public static final String COMMAND_WORD = "edit-student";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the student identified "
            + "by the index number used in the displayed student list. "
            + "Existing values will be overwritten by the input values. "
            + "(note: If only one of the lesson time is to be changed, "
            + "please input the other unchanged lesson time too.)\n"
            + "Parameters: " + PREFIX_INDEX + "INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_LESSON_TIME + "LESSON TIME]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_INDEX + "1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_LESSON_TIME + "1330 Sat";

    public static final String MESSAGE_MIXED_PREFIX = "You cannot mix " + PREFIX_LESSON_TIME
            + " with " + PREFIX_ADD_LESSON_TIME + " or " + PREFIX_DELETE_LESSON_TIME
            + ".\n"
            + "Choose one mode only:\n"
            + "1. use only prefix " + PREFIX_LESSON_TIME + " (replace all lesson time).\n"
            + "2. use a combination of prefix " + PREFIX_ADD_LESSON_TIME + " and/or " + PREFIX_DELETE_LESSON_TIME
            + " (incremental edits).";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Student: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in the student list.";
    public static final String MESSAGE_NO_LESSON_TIMES = "A student must have at least one lesson time.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;
    private String unfoundLessonTimeMessage = "";

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (editedPerson.getLessonTime().isEmpty()) {
            throw new CommandException(MESSAGE_NO_LESSON_TIMES);
        }

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.refreshReminders();
        String msg = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));
        msg = unfoundLessonTimeMessage.isEmpty() ? msg : unfoundLessonTimeMessage + "\n" + msg;
        return new CommandResult(msg);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());

        Set<LessonTime> updateLessonTime = new HashSet<>(personToEdit.getLessonTime());
        if (editPersonDescriptor.getLessonTime().isPresent()) {
            updateLessonTime = editPersonDescriptor.getLessonTime().get();
        } else {
            Set<LessonTime> toRemove = editPersonDescriptor.getLessonTimesToRemove().orElse(Collections.emptySet());
            Set<LessonTime> notFound = new HashSet<>(toRemove);
            notFound.removeAll(updateLessonTime);
            updateLessonTime.removeAll(toRemove);
            editPersonDescriptor.getLessonTimesToAdd().ifPresent(updateLessonTime::addAll);

            if (!notFound.isEmpty()) {
                StringBuilder sb = new StringBuilder("Could not find lesson time: ");
                for (LessonTime lt : notFound) {
                    sb.append(lt.toString()).append(", ");
                }
                sb.deleteCharAt(sb.length() - 1).setCharAt(sb.length() - 1, ';');
                unfoundLessonTimeMessage = sb.toString();
            }
        }

        Person newPerson = new Person(updatedName, updatedPhone, updateLessonTime);
        newPerson.setAllPaymentStatus(personToEdit.getPaymentStatusBitSet());
        newPerson.setHomeworkList(personToEdit.getHomeworkList());
        for (ParticipationRecord r : personToEdit.getParticipation().asList()) {
            try {
                newPerson.getParticipation().add(r);
            } catch (Exception ignored) {
                // skip invalid rows instead of failing whole file
            }
        }
        return newPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Set<LessonTime> lessonTime;
        private Set<LessonTime> lessonTimesToAdd;
        private Set<LessonTime> lessonTimesToRemove;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code lessonTime} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setLessonTime(toCopy.lessonTime);
            setLessonTimesToAdd(toCopy.lessonTimesToAdd);
            setLessonTimesToRemove(toCopy.lessonTimesToRemove);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, lessonTime,
                    lessonTimesToAdd, lessonTimesToRemove);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        /**
         * Sets {@code lessonTime} to this object's {@code lessonTime}.
         * A defensive copy of {@code lessonTime} is used internally.
         */
        public void setLessonTime(Set<LessonTime> lessonTime) {
            this.lessonTime = (lessonTime != null) ? new HashSet<>(lessonTime) : null;
        }

        /**
         * Returns an unmodifiable lesson time set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code lessonTime} is null.
         */
        public Optional<Set<LessonTime>> getLessonTime() {
            return (lessonTime != null) ? Optional.of(Collections.unmodifiableSet(lessonTime)) : Optional.empty();
        }

        /**
         * Sets {@code lessonTime} to this object's {@code lessonTimeToAdd}.
         * A defensive copy of {@code lessonTimeToAdd} is used internally.
         */
        public void setLessonTimesToAdd(Set<LessonTime> lessonTime) {
            this.lessonTimesToAdd = (lessonTime != null) ? new HashSet<>(lessonTime) : null;
        }

        /**
         * Returns an unmodifiable lesson time set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code lessonTimeToAdd} is null.
         */
        public Optional<Set<LessonTime>> getLessonTimesToAdd() {
            return (lessonTimesToAdd != null)
                    ? Optional.of(Collections.unmodifiableSet(lessonTimesToAdd))
                    : Optional.empty();
        }

        /**
         * Sets {@code lessonTime} to this object's {@code lessonTimeToRemove}.
         * A defensive copy of {@code lessonTimeToRemove} is used internally.
         */
        public void setLessonTimesToRemove(Set<LessonTime> lessonTime) {
            this.lessonTimesToRemove = (lessonTime != null) ? new HashSet<>(lessonTime) : null;
        }

        /**
         * Returns an unmodifiable lesson time set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code lessonTimeToRemove} is null.
         */
        public Optional<Set<LessonTime>> getLessonTimesToRemove() {
            return (lessonTimesToRemove != null)
                    ? Optional.of(Collections.unmodifiableSet(lessonTimesToRemove))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(lessonTime, otherEditPersonDescriptor.lessonTime)
                    && Objects.equals(lessonTimesToAdd, otherEditPersonDescriptor.lessonTimesToAdd)
                    && Objects.equals(lessonTimesToRemove, otherEditPersonDescriptor.lessonTimesToRemove);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("lesson time", lessonTime)
                    .add("lessonTimesToAdd", lessonTimesToAdd)
                    .add("lessonTimesToRemove", lessonTimesToRemove)
                    .toString();
        }
    }
}
