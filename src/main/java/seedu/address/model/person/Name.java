package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StringUtil.toTitleCase;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {
    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain letters, spaces, comma, round brackets, hyphens, apostrophes, slash, at sign,"
                + " full stop.\nIt must contain at least one letter, with a maximum length of 50 characters";

    /**
     * Names are stored and displayed in all lowercase.
     */
    public static final String VALIDATION_REGEX = "(?=.*[A-Za-z])[A-Za-z\\s'@/(),.-]{1,50}";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String collapsedName = name.replaceAll("\\s+", " ").trim().toLowerCase();
        checkArgument(isValidName(collapsedName), MESSAGE_CONSTRAINTS);
        fullName = toTitleCase(collapsedName);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        String collapsedName = test.replaceAll("\\s+", " ").trim().toLowerCase();
        return collapsedName.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
}
