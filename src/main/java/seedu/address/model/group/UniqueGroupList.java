package seedu.address.model.group;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of {@link Group} that enforces uniqueness by {@link GroupName} (case-insensitive).
 * <p>
 * Mirrors AB3's {@code UniqueXxxList} pattern:
 * <ul>
 *   <li>All modifying operations validate inputs and preserve the uniqueness invariant.</li>
 *   <li>Provides an unmodifiable {@link ObservableList} view for UI binding.</li>
 * </ul>
 */
public final class UniqueGroupList implements Iterable<Group> {

    private final ObservableList<Group> internalList = FXCollections.observableArrayList();
    private final ObservableList<Group> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns {@code true} if a group with the given {@code name} exists in the list.
     *
     * @param name group name to check (non-null)
     */
    public boolean contains(GroupName name) {
        requireNonNull(name);
        return getByName(name).isPresent();
    }

    /**
     * Returns the group with the given {@code name}.
     *
     * @param name group name to get (non-null)
     * @return group with the given name, wrapped in an {@link Optional}, or empty if not found
     */
    public Optional<Group> getByName(GroupName name) {
        requireNonNull(name);
        return internalList.stream().filter(g -> g.getName().equals(name)).findFirst();
    }

    /**
     * Adds the given {@code toAdd} to the list.
     *
     * @param toAdd group to add (non-null)
     * @throws IllegalArgumentException if a group with the same {@link GroupName} already exists
     */
    public void add(Group toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd.getName())) {
            throw new IllegalArgumentException("Duplicate group: " + toAdd);
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the group that has the given {@code name}.
     *
     * @param name group name to remove (non-null)
     * @throws IllegalArgumentException if no such group exists
     */
    public void remove(GroupName name) {
        requireNonNull(name);
        boolean removed = internalList.removeIf(g -> g.getName().equals(name));
        if (!removed) {
            throw new IllegalArgumentException("Group not found: " + name);
        }
    }

    /**
     * Replaces the contents of this list with {@code groups}.
     * <p>
     * The provided list must itself be free of duplicates by {@link GroupName}.
     *
     * @param groups replacement list (non-null)
     * @throws IllegalArgumentException if {@code groups} contains duplicates
     */
    public void setGroups(List<Group> groups) {
        requireNonNull(groups);
        internalList.setAll(groups);
        long distinct = internalList.stream().map(g -> g.getName().key()).distinct().count();
        if (distinct != internalList.size()) {
            throw new IllegalArgumentException("Groups contain duplicates");
        }
    }

    /**
     * Returns an unmodifiable observable view of the groups for UI binding.
     */
    public ObservableList<Group> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Returns an iterator over the current snapshot of groups.
     * The returned iterator does not support modification.
     */
    @Override
    public Iterator<Group> iterator() {
        return internalUnmodifiableList.iterator();
    }

    /**
     * Returns {@code true} if both lists contain the same groups in the same order.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UniqueGroupList
                && internalList.equals(((UniqueGroupList) other).internalList));
    }

    /**
     * Returns a hash code based on the contained groups.
     */
    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
