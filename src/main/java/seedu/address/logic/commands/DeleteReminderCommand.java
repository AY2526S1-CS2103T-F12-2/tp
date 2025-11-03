package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_REMINDERS;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.reminder.ReminderFieldsContainsKeywordsPredicate;
import seedu.address.model.reminder.UnmodifiableReminder;

/**
 * Deletes a reminder identified using it's displayed index from the reminder list.
 */
public class DeleteReminderCommand extends Command {
    public static final String COMMAND_WORD = "delete-reminder";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a reminder either by index or by search keyword.\n"
            + "Parameters:\n"
            + "  " + PREFIX_INDEX + "INDEX (positive integer), OR\n"
            + "  " + PREFIX_KEYWORD + "KEYWORD(S)\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " " + PREFIX_INDEX + "1\n"
            + "  " + COMMAND_WORD + " " + PREFIX_KEYWORD + "marcus ng";

    public static final String MESSAGE_DELETE_REMINDER_SUCCESS = "Reminder Deleted: %1$s";
    public static final String MESSAGE_MULTIPLE_MATCHES =
            "Multiple reminders match the given keyword(s). Please refine your search:\n";
    public static final String MESSAGE_NO_MATCH = "No reminders match the given keyword(s).";

    private final Optional<Index> targetIndex;
    private final Optional<ReminderFieldsContainsKeywordsPredicate> predicate;

    /**
     * Constructs a {@code DeleteCommand} to delete a reminder by their index.
     *
     * @param targetIndex the index of the reminder in the displayed list to delete
     */
    public DeleteReminderCommand(Index targetIndex) {
        this.targetIndex = Optional.of(targetIndex);
        this.predicate = Optional.empty();
    }

    /**
     * Constructs a {@code DeleteCommand} to delete reminders matching a given predicate.
     *
     * @param predicate the predicate used to filter which reminders to delete
     */
    public DeleteReminderCommand(ReminderFieldsContainsKeywordsPredicate predicate) {
        this.targetIndex = Optional.empty();
        this.predicate = Optional.of(predicate);
    }

    /**
     * Executes the delete command.
     *
     * @param model the {@code Model} containing the reminder list to modify.
     * @return a {@code CommandResult} indicating successful deletion.
     * @throws CommandException if the given index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // deleting by index
        if (targetIndex.isPresent()) {
            Reminder reminderToDelete = getReminderByIndex(model);
            model.deleteReminder(reminderToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_REMINDER_SUCCESS, Messages.format(reminderToDelete)));
        }

        // deleting by keywords
        ReminderFieldsContainsKeywordsPredicate pred = predicate.get();
        Predicate<Reminder> modifiableFilter = Reminder::isModifiable;
        Predicate<Reminder> combined = pred.and(modifiableFilter);
        model.updateFilteredReminderList(combined);
        List<Reminder> matches = model.getFilteredReminderList();

        if (matches.isEmpty()) {
            return new CommandResult(MESSAGE_NO_MATCH);
        }

        String keyword = String.join(" ", pred.getKeywords()).trim().toLowerCase();

        // due date
        List<Reminder> exactDueDateMatches = matches.stream()
                .filter(p -> p.getDueDate().toString().trim().toLowerCase().contains(keyword))
                .toList();

        // delete if match
        if (exactDueDateMatches.size() == 1) {
            Reminder reminderToDelete = isModifiableReminder(exactDueDateMatches.get(0));
            model.deleteReminder(reminderToDelete);
            model.updateFilteredReminderList(PREDICATE_SHOW_ALL_REMINDERS);
            return new CommandResult(String.format(MESSAGE_DELETE_REMINDER_SUCCESS, Messages.format(reminderToDelete)));
        }

        List<Reminder> refinementCandidates = exactDueDateMatches.isEmpty() ? matches : exactDueDateMatches;

        // description
        if (exactDueDateMatches.isEmpty()) {
            List<Reminder> exactDescriptionMatches = matches.stream()
                    .filter(p -> p.getDescription().toString().trim().toLowerCase().contains(keyword))
                    .toList();

            if (exactDescriptionMatches.size() == 1) {
                Reminder reminderToDelete = isModifiableReminder(exactDescriptionMatches.get(0));
                model.deleteReminder(reminderToDelete);
                model.updateFilteredReminderList(PREDICATE_SHOW_ALL_REMINDERS);
                return new CommandResult(String.format(MESSAGE_DELETE_REMINDER_SUCCESS,
                        Messages.format(reminderToDelete)));
            } else {
                refinementCandidates = exactDescriptionMatches;
            }
        }

        String matchString = generateAllMatches(refinementCandidates);
        return new CommandResult(matchString);
    }

    /**
     * Generates the string of all matched reminders after filtering out by keyword. UnmodifiableReminders
     * are not included in the string, since they cannot be deleted.
     */
    private String generateAllMatches(List<Reminder> refinementCandidates) {
        if (refinementCandidates.isEmpty()) {
            return MESSAGE_NO_MATCH;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < refinementCandidates.size(); i++) {
            Reminder reminder = refinementCandidates.get(i);
            if (!reminder.isModifiable()) {
                continue;
            }
            sb.append(String.format("%d. %s%n", i + 1, Messages.format(reminder)));
        }

        if (sb.isEmpty()) {
            return MESSAGE_NO_MATCH;
        } else {
            return MESSAGE_MULTIPLE_MATCHES + sb + "\nTry typing the exact date time or description.";
        }
    }

    /**
     * Retrieves the reminder to delete from the model based on the provided index.
     *
     * @throws CommandException if the index is invalid or the reminder is unmodifiable.
     */
    private Reminder getReminderByIndex(Model model) throws CommandException {
        List<Reminder> lastShownList = model.getFilteredReminderList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
        }

        Index index = targetIndex.get();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_OUT_OF_BOUNDS_DELETE_INDEX,
                    index.getOneBased(), lastShownList.size())
            );
        }

        Reminder reminderToDelete = lastShownList.get(index.getZeroBased());
        return isModifiableReminder(reminderToDelete);
    }

    /**
     * Returns {@code reminderToDelete} if it is not an instance of UnmodifiableReminder. Otherwise,
     * throw CommandException.
     */
    private Reminder isModifiableReminder(Reminder reminderToDelete) throws CommandException {
        if (!reminderToDelete.isModifiable()) {
            assert reminderToDelete instanceof UnmodifiableReminder
                    : "Reminder should be UnmodifiableReminder if it is not modifiable.";

            UnmodifiableReminder unmodifiableReminder = (UnmodifiableReminder) reminderToDelete;
            throw new CommandException(Messages.MESSAGE_UNMODIFIABLE_REMINDER + "\n"
                    + unmodifiableReminder.getModifyMessage());
        }
        return reminderToDelete;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteReminderCommand)) {
            return false;
        }

        DeleteReminderCommand otherDeleteCommand = (DeleteReminderCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
