package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESC;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.reminder.Reminder;

/**
 * Adds a reminder to the reminder list.
 */
public class AddReminderCommand extends Command {
    public static final String COMMAND_WORD = "add-reminder";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a reminder to the address book. "
            + "Parameters: "
            + PREFIX_DATE + "DUE_DATE "
            + PREFIX_DESC + "DESCRIPTION\n"
            + "(note: DUE_DATE can be in YYYY-MM-DD or YYYY-MM-DD HHMM format."
            + "eg. 2025-10-10 or 2025-10-10 1010)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "2025-10-12 1500 "
            + PREFIX_DESC + "Tuition later at 3pm";

    public static final String MESSAGE_SUCCESS = "Reminder added. %1$s";
    public static final String MESSAGE_DUPLICATE_REMINDER = "This reminder already exists in the reminder list";
    public static final String MESSAGE_OVERDUE_REMINDER = "Reminders must be set for a future date or time";

    private final Reminder toAdd;

    /**
     * Creates an ReminderCommand to add the specified {@code Reminder}
     */
    public AddReminderCommand(Reminder reminder) {
        requireNonNull(reminder);
        toAdd = reminder;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (toAdd.isOverdue()) {
            throw new CommandException(MESSAGE_OVERDUE_REMINDER);
        }

        if (model.hasReminder(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_REMINDER);
        }

        model.addReminder(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddReminderCommand)) {
            return false;
        }

        AddReminderCommand otherAddReminderCommand = (AddReminderCommand) other;
        return toAdd.equals(otherAddReminderCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
