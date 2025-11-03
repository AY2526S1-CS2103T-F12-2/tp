package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.reminder.Reminder;

/**
 * A utility class containing a list of {@code Reminder} objects to be used in tests.
 */
public class TypicalReminders {
    public static final Reminder REMINDER_1 = new ReminderBuilder().withDueDate("2025-10-21 1530")
            .withDescription("Alice: Submit homework for Math").build();
    public static final Reminder REMINDER_2 = new ReminderBuilder().withDueDate("2025-10-22 1000")
            .withDescription("Benson: Prepare for English lesson").build();
    public static final Reminder REMINDER_3 = new ReminderBuilder().withDueDate("2025-10-25")
            .withDescription("Carl: Assignment review").build();
    public static final Reminder REMINDER_4 = new ReminderBuilder().withDueDate("2026-11-01")
            .withDescription("Daniel: Feedback session").build();

    private TypicalReminders() {
    } // prevents instantiation

    public static List<Reminder> getTypicalReminders() {
        return new ArrayList<>(Arrays.asList(REMINDER_1, REMINDER_2, REMINDER_3, REMINDER_4));
    }
}
