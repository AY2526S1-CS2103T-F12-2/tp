package seedu.address.logic.commands.homeworkcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.homework.Homework;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;


/**
 * Adds a homework item to a students homework list (for now it works only for name but will add for group when
 * that function is done)
 */
public class AddHomeworkCommand extends Command {
    public static final String COMMAND_WORD = "add-homework";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a homework item for a student. \n"
            + "Command format: " + COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + PREFIX_DESC + "DESCRIPTION "
            + PREFIX_DEADLINE + "DEADLINE (yyyy-mm-dd)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " Marcus "
            + PREFIX_DESC + "Math Worksheet 1 "
            + PREFIX_DEADLINE + "2025-10-27";

    public static final String MESSAGE_SUCCESS = "Added homework for %1$s: %2$s (due %3$s)";
    public static final String MESSAGE_NO_PERSON_FOUND = "No student with given name";
    public static final String MESSAGE_DUPLICATE_HOMEWORK = "This student has already been assigned this homework";
    public static final String MESSAGE_DESC_TOO_LONG = "Description is too long(max 50 Characters)";
    public static final String MESSAGE_DUE_DATE_PASSED = "Warning: Deadline is before current date";

    private final Name studentName;
    private final Homework homework;

    /**
     * Creates a AddHomeworkCommand to add Homework to a students list
     */
    public AddHomeworkCommand(Name studentName, Homework homework) {
        requireNonNull(studentName);
        requireNonNull(homework);
        this.studentName = studentName;
        this.homework = homework;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        //Able to add homework after search command
        Person target = getPerson(model);

        String resultMessage = String.format(
                MESSAGE_SUCCESS,
                target.getName().fullName,
                homework.getDescription(),
                homework.getDeadline()
        );

        if (homework.getDeadline().isBefore(java.time.LocalDate.now())) {
            resultMessage += "\n⚠️ " + MESSAGE_DUE_DATE_PASSED;
        }

        target.addHomework(homework);
        model.refreshReminders();

        return new CommandResult(resultMessage);

    }

    /**
     * Retrieves the person to add homework to from the model based on the provided student name.
     *
     * @throws CommandException if the student is not found or homework list of the student already
     *                          contains the homework.
     */
    private Person getPerson(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        Person target = null;
        for (Person p : lastShownList) {
            if (p.getName().equals(studentName)) {
                target = p;
                break;
            }
        }

        if (target == null) {
            throw new CommandException(MESSAGE_NO_PERSON_FOUND);
        }

        if (target.getHomeworkList().contains(homework)) {
            throw new CommandException(MESSAGE_DUPLICATE_HOMEWORK);
        }
        return target;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddHomeworkCommand)) {
            return false;
        }

        AddHomeworkCommand otherCommand = (AddHomeworkCommand) other;
        return studentName.equals(otherCommand.studentName)
                && homework.equals(otherCommand.homework);
    }
}
