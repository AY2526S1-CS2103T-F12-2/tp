package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentFieldsContainsKeywordsPredicate;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete-student";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a student either by index or by search keyword.\n"
            + "Parameters:\n"
            + "  " + PREFIX_INDEX + "INDEX (positive integer), OR\n"
            + "  " + PREFIX_KEYWORD + "KEYWORD(S)\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " " + PREFIX_INDEX + "1\n"
            + "  " + COMMAND_WORD + " " + PREFIX_KEYWORD + "marcus ng";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Student Deleted: %1$s";
    public static final String MESSAGE_MULTIPLE_MATCHES =
            "Multiple students match the given keyword(s). Please refine your search:\n";
    public static final String MESSAGE_NO_MATCH = "No students match the given keyword(s).";


    private final Optional<Index> targetIndex;
    private final Optional<StudentFieldsContainsKeywordsPredicate> predicate;

    /**
     * Constructs a {@code DeleteCommand} to delete a student by their index.
     *
     * @param targetIndex the index of the student in the displayed list to delete
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = Optional.of(targetIndex);
        this.predicate = Optional.empty();
    }

    /**
     * Constructs a {@code DeleteCommand} to delete students matching a given predicate.
     *
     * @param predicate the predicate used to filter which students to delete
     */
    public DeleteCommand(StudentFieldsContainsKeywordsPredicate predicate) {
        this.targetIndex = Optional.empty();
        this.predicate = Optional.of(predicate);
    }

    /**
     * Executes the delete command.
     *
     * @param model the {@code Model} containing the student list to modify.
     * @return a {@code CommandResult} indicating successful deletion.
     * @throws CommandException if the given index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // deleting by index
        if (targetIndex.isPresent()) {
            Person personToDelete = getPersonToDeleteByIndex(model);
            model.deletePerson(personToDelete);
            model.refreshReminders();
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        // deleting by keywords
        StudentFieldsContainsKeywordsPredicate pred = predicate.get();
        model.updateFilteredPersonList(pred);
        List<Person> matches = model.getFilteredPersonList();

        if (matches.isEmpty()) {
            return new CommandResult(MESSAGE_NO_MATCH);
        }

        String keyword = String.join(" ", pred.getKeywords()).trim().toLowerCase();

        // name
        List<Person> exactNameMatches = matches.stream()
                .filter(p -> p.getName().fullName.trim().toLowerCase().equals(keyword))
                .toList();

        // delete if match
        if (exactNameMatches.size() == 1) {
            Person personToDelete = exactNameMatches.get(0);
            model.deletePerson(personToDelete);
            model.refreshReminders();
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        List<Person> refinementCandidates = exactNameMatches.isEmpty() ? matches : exactNameMatches;

        // phone number
        if (exactNameMatches.isEmpty()) {
            List<Person> exactPhoneMatches = matches.stream()
                    .filter(p -> p.getPhone().value.trim().equalsIgnoreCase(keyword))
                    .toList();

            if (exactPhoneMatches.size() == 1) {
                Person personToDelete = exactPhoneMatches.get(0);
                model.deletePerson(personToDelete);
                model.refreshReminders();
                return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
            } else if (exactPhoneMatches.size() > 1) {
                refinementCandidates = exactPhoneMatches;
            }

            // lesson time match
            if (exactPhoneMatches.isEmpty()) {
                List<Person> exactLessonMatches = matches.stream()
                        .filter(p -> p.getLessonTime() != null
                                && p.getLessonTime().stream()
                                .map(lt -> lt.toString().trim().toLowerCase())
                                .anyMatch(t -> t.equals(keyword)))
                        .toList();

                if (exactLessonMatches.size() == 1) {
                    Person personToDelete = exactLessonMatches.get(0);
                    model.deletePerson(personToDelete);
                    model.refreshReminders();
                    return new CommandResult(String.format(
                            MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
                } else if (exactLessonMatches.size() > 1) {
                    refinementCandidates = exactLessonMatches;
                }
            }
        }

        if (refinementCandidates.isEmpty()) {
            return new CommandResult(MESSAGE_NO_MATCH);
        }

        if (refinementCandidates.size() > 1) {
            StringBuilder sb = new StringBuilder(MESSAGE_MULTIPLE_MATCHES);
            for (int i = 0; i < refinementCandidates.size(); i++) {
                sb.append(String.format("%d. %s%n", i + 1, Messages.format(refinementCandidates.get(i))));
            }
            sb.append("\nTry typing the exact name, phone number, or lesson time.");
            return new CommandResult(sb.toString());
        }

        Person personToDelete = refinementCandidates.get(0);
        model.deletePerson(personToDelete);
        model.refreshReminders();
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    /**
     * Retrieves the person to delete from the model based on the provided index.
     *
     * @throws CommandException if the index is invalid.
     */
    private Person getPersonToDeleteByIndex(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Index index = targetIndex.get();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_OUT_OF_BOUNDS_DELETE_INDEX,
                    index.getOneBased(), lastShownList.size())
            );
        }

        return lastShownList.get(index.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
