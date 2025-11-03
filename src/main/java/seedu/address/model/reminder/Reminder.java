package seedu.address.model.reminder;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Reminder in the reminder list.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Reminder implements Comparable<Reminder> {
    private final DueDate dueDate;
    private final Description description;

    /**
     * Every field must be present and not null.
     */
    public Reminder(DueDate dueDate, Description description) {
        requireAllNonNull(dueDate, description);
        this.dueDate = dueDate;
        this.description = description;
    }

    public DueDate getDueDate() {
        return dueDate;
    }

    public Description getDescription() {
        return description;
    }

    /**
     * Returns true always for general reminder.
     */
    public boolean isModifiable() {
        return true;
    }

    /**
     * Returns true if {@code reminder} is a past event.
     */
    public boolean isOverdue() {
        return getDueDate().isBeforeDate(LocalDateTime.now());
    }

    /**
     * Returns the number of days until this reminder is due.
     */
    public long daysUntilDueDate() {
        LocalDateTime now = LocalDateTime.now();
        return dueDate.daysUntilDue(now);
    }

    /**
     * Returns 1 if this reminder is past but other is upcoming; Returns -1 if this reminder is
     * upcoming and other is past; If both reminders are in the same category (both past or both
     * upcoming), sort by due date ascending.
     */
    @Override
    public int compareTo(Reminder other) {
        LocalDateTime now = LocalDateTime.now();
        boolean r1Past = dueDate.isBeforeDate(now);
        boolean r2Past = other.dueDate.isBeforeDate(now);

        if (r1Past && !r2Past) {
            return 1;
        } else if (!r1Past && r2Past) {
            return -1;
        } else {
            return this.dueDate.compareTo(other.dueDate);
        }
    }

    /**
     * Returns true if both reminders have the same due date and detail.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Reminder)) {
            return false;
        }

        Reminder otherReminder = (Reminder) other;
        return otherReminder.getDueDate().equals(getDueDate())
                && otherReminder.getDescription().equals(getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(dueDate, description);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("dueDate", dueDate)
                .add("description", description)
                .toString();
    }
}
