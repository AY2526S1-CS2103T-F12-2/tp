package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Person;

/**
 * Removes one or more students from a group.
 * <p>
 * Format: {@code group-remove g/GROUP i/INDEX [i/INDEX ...]}
 */
public class GroupRemoveCommand extends Command {

    /** Command word for removing members from a group. */
    public static final String COMMAND_WORD = "group-remove";

    /** Usage message shown on format errors. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes students from a group.\n"
            + "Parameters: g/GROUP i/INDEX...\n"
            + "Example: " + COMMAND_WORD + " g/Group A i/2 i/3";

    /** Success and info message templates. */
    public static final String MESSAGE_REMOVED_FMT = "Removed %1$d student(s) from %2$s";
    public static final String MESSAGE_NO_CHANGES_FMT = "No changes: no members were removed from %s.";
    public static final String MESSAGE_NOT_IN_GROUP_FMT = "Not in group (skipped): %s";
    public static final String MESSAGE_SKIPPED_DUPLICATE_INDICES_FMT = "Skipped duplicate indices: %s";
    /** Error shown when the referenced group does not exist. */
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group \"%1$s\" not found.";

    private final GroupName groupName;
    private final List<Index> targetIndices;

    /**
     * Constructs a command that removes the given displayed-list {@code targetIndices} from {@code groupName}.
     *
     * @param groupName     validated group name to remove members from (non-null).
     * @param targetIndices one or more 1-based indices from the current filtered person list (non-null).
     */
    public GroupRemoveCommand(GroupName groupName, List<Index> targetIndices) {
        this.groupName = requireNonNull(groupName);
        this.targetIndices = List.copyOf(requireNonNull(targetIndices));
    }

    /**
     * Executes the command to remove the specified displayed-list indices from the target group.
     *
     * @param model backing model (non-null)
     * @return command result with a summary message
     * @throws CommandException if the group does not exist or any index is invalid
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasGroup(groupName)) {
            throw new CommandException(String.format(MESSAGE_GROUP_NOT_FOUND, groupName));
        }

        var shown = model.getFilteredPersonList();
        if (shown.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        // De-duplicate indices, validate bounds, and build unique target list
        java.util.Set<Integer> seenZero = new java.util.LinkedHashSet<>();
        java.util.List<String> duplicateTokens = new java.util.ArrayList<>();
        java.util.List<Person> uniqueTargets = new java.util.ArrayList<>();

        for (Index idx : targetIndices) {
            int z = idx.getZeroBased();
            if (z < 0 || z >= shown.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
            }
            if (!seenZero.add(z)) {
                duplicateTokens.add("i/" + idx.getOneBased());
                continue;
            }
            uniqueTargets.add(shown.get(z));
        }

        // Split into (in group) vs (not in group)
        var inGroup = new java.util.ArrayList<Person>();
        var notInGroup = new java.util.ArrayList<Person>();
        for (Person p : uniqueTargets) {
            if (model.getGroupsOf(p).contains(groupName)) {
                inGroup.add(p);
            } else {
                notInGroup.add(p);
            }
        }

        // Apply removal only for those who are actually members
        if (!inGroup.isEmpty()) {
            model.removeFromGroup(groupName, inGroup);
        }

        // Build truthful feedback
        var lines = new java.util.ArrayList<String>();
        if (!inGroup.isEmpty()) {
            lines.add(String.format(MESSAGE_REMOVED_FMT, inGroup.size(), groupName));
        } else {
            lines.add(String.format(MESSAGE_NO_CHANGES_FMT, groupName));
        }
        if (!duplicateTokens.isEmpty()) {
            lines.add(String.format(MESSAGE_SKIPPED_DUPLICATE_INDICES_FMT, String.join(", ", duplicateTokens)));
        }
        if (!notInGroup.isEmpty()) {
            lines.add(String.format(
                    MESSAGE_NOT_IN_GROUP_FMT,
                    notInGroup.stream().map(x -> x.getName().fullName).collect(
                        java.util.stream.Collectors.joining(", ")))
            );
        }

        return new CommandResult(String.join("\n", lines));
    }

    /**
     * Returns true if both commands target the same group and indices.
     *
     * @param other other object
     * @return equality as per group name and indices
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof GroupRemoveCommand)
                && groupName.equals(((GroupRemoveCommand) other).groupName)
                && targetIndices.equals(((GroupRemoveCommand) other).targetIndices);
    }
}
