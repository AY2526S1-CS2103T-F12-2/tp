package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Person;

/**
 * Adds one or more students to a group.
 * <p>
 * Format: {@code group-add g/GROUP i/INDEX [i/INDEX ...]}
 */
public class GroupAddCommand extends Command {

    /** Command word for adding members to a group. */
    public static final String COMMAND_WORD = "group-add";

    /** Usage message shown on format errors. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds students to a group.\n"
            + "Parameters: g/GROUP i/INDEX...\n"
            + "Example: " + COMMAND_WORD + " g/Group A i/1 i/3 i/4";

    /** Summary message templates. */
    public static final String MESSAGE_ADDED_FMT = "Added %d student(s) to %s";
    public static final String MESSAGE_NO_CHANGES_FMT = "No changes: no new members were added to %s.";
    public static final String MESSAGE_SKIPPED_DUPLICATE_INDICES_FMT = "Skipped duplicate indices: %s";
    public static final String MESSAGE_ALREADY_IN_GROUP_FMT = "Already in group (unchanged): %s";
    public static final String MESSAGE_INVALID_INDICES_FMT = "Invalid indices (out of range): %s";

    /** Error shown when the referenced group does not exist. */
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group \"%1$s\" not found.";

    private final GroupName groupName;
    private final List<Index> targetIndices;

    /**
     * Constructs a command that adds the given displayed-list {@code targetIndices} to {@code groupName}.
     *
     * @param groupName     validated group name to add members to (non-null).
     * @param targetIndices one or more 1-based indices from the current filtered person list (non-null).
     */
    public GroupAddCommand(GroupName groupName, List<Index> targetIndices) {
        this.groupName = requireNonNull(groupName);
        this.targetIndices = List.copyOf(requireNonNull(targetIndices));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasGroup(groupName)) {
            throw new CommandException(String.format(MESSAGE_GROUP_NOT_FOUND, groupName));
        }

        final List<Person> shown = model.getFilteredPersonList();
        if (shown.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        // Track duplicates/invalids while preserving first-seen order
        final Set<Integer> seenZeroBased = new LinkedHashSet<>();
        final List<String> duplicateTokens = new ArrayList<>();
        final List<String> invalidTokens = new ArrayList<>();
        final List<Person> uniqueTargets = new LinkedList<>();

        for (Index idx : targetIndices) {
            int z = idx.getZeroBased();
            // duplicated in the same command
            if (!seenZeroBased.add(z)) {
                duplicateTokens.add("i/" + idx.getOneBased());
                continue;
            }
            // invalid/out of range
            if (z < 0 || z >= shown.size()) {
                invalidTokens.add("i/" + idx.getOneBased());
                continue;
            }
            uniqueTargets.add(shown.get(z));
        }

        // Separate into those already in group vs those to add
        final List<Person> alreadyMembers = new ArrayList<>();
        final List<Person> toAdd = new ArrayList<>();
        for (Person p : uniqueTargets) {
            // Assumes Model#getGroupsOf(Person) exists and returns Set<GroupName>
            if (model.getGroupsOf(p).contains(groupName)) {
                alreadyMembers.add(p);
            } else {
                toAdd.add(p);
            }
        }

        if (!toAdd.isEmpty()) {
            model.addToGroup(groupName, toAdd);
        }

        // Build truthful multi-line result message
        List<String> lines = new ArrayList<>();
        if (!toAdd.isEmpty()) {
            lines.add(String.format(MESSAGE_ADDED_FMT, toAdd.size(), groupName,
                    toAdd.stream().map(x -> x.getName().fullName).collect(Collectors.joining(", "))));
        } else {
            lines.add(String.format(MESSAGE_NO_CHANGES_FMT, groupName));
        }

        if (!duplicateTokens.isEmpty()) {
            lines.add(String.format(MESSAGE_SKIPPED_DUPLICATE_INDICES_FMT, String.join(", ", duplicateTokens)));
        }
        if (!alreadyMembers.isEmpty()) {
            lines.add(String.format(MESSAGE_ALREADY_IN_GROUP_FMT,
                    alreadyMembers.stream().map(x -> x.getName().fullName).collect(Collectors.joining(", "))));
        }
        if (!invalidTokens.isEmpty()) {
            lines.add(String.format(MESSAGE_INVALID_INDICES_FMT, String.join(", ", invalidTokens)));
        }

        return new CommandResult(String.join("\n", lines));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof GroupAddCommand)
                && groupName.equals(((GroupAddCommand) other).groupName)
                && targetIndices.equals(((GroupAddCommand) other).targetIndices);
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "{group=" + groupName + ", indices=" + targetIndices + "}";
    }
}
