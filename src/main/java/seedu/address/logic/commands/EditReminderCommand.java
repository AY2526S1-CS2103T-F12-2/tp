package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.AddReminderCommand.MESSAGE_OVERDUE_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_REMINDERS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.reminder.Description;
import seedu.address.model.reminder.DueDate;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.reminder.UnmodifiableReminder;

/**
 * Edits the details of an existing reminder in the reminder list.
 */
public class EditReminderCommand extends Command {
    public static final String COMMAND_WORD = "edit-reminder";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the reminder identified "
            + "by the index number used in the displayed reminder list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + PREFIX_INDEX + "INDEX (must be a positive integer) "
            + "[" + PREFIX_DATE + "DUE_DATE] "
            + "[" + PREFIX_DESC + "DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_INDEX + "1 "
            + PREFIX_DATE + "2025-12-25 "
            + PREFIX_DESC + "Christmas";

    public static final String MESSAGE_EDIT_REMINDER_SUCCESS = "Edited Reminder: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_REMINDER = "This reminder already exists in the reminder list";

    private final Index index;
    private final EditReminderDescriptor editReminderDescriptor;

    /**
     * @param index                  of the reminder in the filtered reminder list to edit
     * @param editReminderDescriptor details to edit the reminder with
     */
    public EditReminderCommand(Index index, EditReminderDescriptor editReminderDescriptor) {
        requireNonNull(index);
        requireNonNull(editReminderDescriptor);

        this.index = index;
        this.editReminderDescriptor = new EditReminderDescriptor(editReminderDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Reminder reminderToEdit = getReminder(model);

        Reminder editedReminder = createEditedReminder(reminderToEdit, editReminderDescriptor);
        if (editedReminder.isOverdue()) {
            throw new CommandException(MESSAGE_OVERDUE_REMINDER);
        }

        if (!reminderToEdit.equals(editedReminder) && model.hasReminder(editedReminder)) {
            throw new CommandException(MESSAGE_DUPLICATE_REMINDER);
        }

        model.setReminder(reminderToEdit, editedReminder);
        model.updateFilteredReminderList(PREDICATE_SHOW_ALL_REMINDERS);
        return new CommandResult(String.format(MESSAGE_EDIT_REMINDER_SUCCESS, Messages.format(editedReminder)));
    }

    /**
     * Retrieves the reminder to edit from the model based on the provided index.
     *
     * @throws CommandException if the index is invalid or the reminder is unmodifiable.
     */
    private Reminder getReminder(Model model) throws CommandException {
        List<Reminder> lastShownList = model.getFilteredReminderList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
        }

        Reminder reminderToEdit = lastShownList.get(index.getZeroBased());
        if (!reminderToEdit.isModifiable()) {
            assert reminderToEdit instanceof UnmodifiableReminder
                    : "Reminder should be UnmodifiableReminder if it is not modifiable.";

            UnmodifiableReminder unmodifiableReminder = (UnmodifiableReminder) reminderToEdit;
            throw new CommandException(Messages.MESSAGE_UNMODIFIABLE_REMINDER + "\n"
                    + unmodifiableReminder.getModifyMessage());
        }
        return reminderToEdit;
    }

    /**
     * Creates and returns a {@code Reminder} with the details of {@code reminderToEdit}
     * edited with {@code editReminderDescriptor}.
     */
    private static Reminder createEditedReminder(
            Reminder reminderToEdit, EditReminderDescriptor editReminderDescriptor) {
        assert reminderToEdit != null;

        DueDate updatedDueDate = editReminderDescriptor.getDueDate().orElse(reminderToEdit.getDueDate());
        Description updatedDescription =
                editReminderDescriptor.getDescription().orElse(reminderToEdit.getDescription());

        return new Reminder(updatedDueDate, updatedDescription);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditReminderCommand)) {
            return false;
        }

        EditReminderCommand otherEditCommand = (EditReminderCommand) other;
        return index.equals(otherEditCommand.index)
                && editReminderDescriptor.equals(otherEditCommand.editReminderDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editReminderDescriptor", editReminderDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the reminder with. Each non-empty field value will replace the
     * corresponding field value of the reminder.
     */
    public static class EditReminderDescriptor {
        private DueDate dueDate;
        private Description description;

        public EditReminderDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditReminderDescriptor(EditReminderDescriptor toCopy) {
            setDueDate(toCopy.dueDate);
            setDescription(toCopy.description);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(dueDate, description);
        }

        public void setDueDate(DueDate dueDate) {
            this.dueDate = dueDate;
        }

        public Optional<DueDate> getDueDate() {
            return Optional.ofNullable(dueDate);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditReminderDescriptor)) {
                return false;
            }

            EditReminderDescriptor otherEditReminderDescriptor = (EditReminderDescriptor) other;
            return Objects.equals(dueDate, otherEditReminderDescriptor.dueDate)
                    && Objects.equals(description, otherEditReminderDescriptor.description);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("dueDate", dueDate)
                    .add("description", description)
                    .toString();
        }
    }
}
