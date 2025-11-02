package seedu.address.model.reminder;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Reminder's description in the reminder list.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {
    public static final String MESSAGE_CONSTRAINTS = "Description should not be empty, must contain at least"
            + " one letter or number, and with a maximum length of 200 characters";
    public static final String VALID_DESC_REGEX = ".*[A-Za-z0-9].*";
    public static final int MAX_LENGTH = 200;

    private final String description;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description string.
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description.trim()), MESSAGE_CONSTRAINTS);
        this.description = description.trim();
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidDescription(String test) {
        return test != null && !test.trim().isEmpty() && test.matches(VALID_DESC_REGEX) && test.length() <= MAX_LENGTH;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Description)) {
            return false;
        }

        Description otherDescription = (Description) other;
        return otherDescription.description.equalsIgnoreCase(description);
    }

    @Override
    public int hashCode() {
        return description.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return description;
    }
}
