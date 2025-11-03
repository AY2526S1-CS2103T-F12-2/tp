package seedu.address.model.group;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Person;

/**
 * A first-class group. Wraps a {@link GroupName} and keeps member references.
 */
public final class Group {

    private final GroupName name;
    private final Set<Person> members = new HashSet<>();

    public Group(GroupName name) {
        this.name = requireNonNull(name);
    }

    public GroupName getName() {
        return name;
    }

    /** Returns an unmodifiable view of the members. */
    public Set<Person> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    /** Adds all given members. */
    public void addMembers(Iterable<Person> toAdd) {
        for (Person p : toAdd) {
            members.add(p);
        }
    }

    /** Removes all given members. */
    public void removeMembers(Iterable<Person> toRemove) {
        for (Person p : toRemove) {
            members.remove(p);
        }
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object other) {
        return (this == other) || (other instanceof Group && name.equals(((Group) other).name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
