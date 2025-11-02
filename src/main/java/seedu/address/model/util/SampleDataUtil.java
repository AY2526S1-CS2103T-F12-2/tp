package seedu.address.model.util;

import static seedu.address.model.reminder.UniqueReminderList.createHomeworkReminder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.homework.Homework;
import seedu.address.model.person.LessonTime;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.reminder.Description;
import seedu.address.model.reminder.DueDate;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.reminder.UnmodifiablePaymentReminder;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Homework[] getSampleHomework() {
        return new Homework[]{
            new Homework("Math: Complete chapter 5 exercises", LocalDate.parse("2025-11-03")),
            new Homework("English: Write a short story", LocalDate.parse("2025-11-04")),
            new Homework("Science: Prepare lab report", LocalDate.parse("2025-11-06")),
            new Homework("History: Read chapter 8 and 9", LocalDate.parse("2025-11-07")),
            new Homework("Math: Solve past year paper", LocalDate.parse("2025-11-10")),
            new Homework("English: Revise grammar notes", LocalDate.parse("2025-11-11")),
            new Homework("Science: Group project submission", LocalDate.parse("2025-11-13")),
            new Homework("History: Create timeline for WW2", LocalDate.parse("2025-11-14"))
        };
    }

    public static Person[] getSamplePersons() {
        return new Person[]{
            new Person(new Name("Alex Yeoh"), new Phone("87438807"),
                    getLessonTimeSet("1000 Sun")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"),
                    getLessonTimeSet("1400 Sat", "1600 Sun")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    getLessonTimeSet("1200 Mon")),
            new Person(new Name("David Li"), new Phone("91031282"),
                    getLessonTimeSet("1300 Wed", "1500 Fri", "1700 Sun")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                    getLessonTimeSet("0900 Tue")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                    getLessonTimeSet("1100 Thu", "1800 Sat"))
        };
    }

    public static Reminder[] getSampleReminders() {
        return new Reminder[]{
            new Reminder(new DueDate("2025-11-11 1530"), new Description("Alice: Submit homework for Math")),
            new Reminder(new DueDate("2025-11-12 1000"), new Description("Benson: Prepare for English lesson")),
            new Reminder(new DueDate("2025-11-12"), new Description("Carl: Assignment review")),
            new Reminder(new DueDate("2025-11-13"), new Description("Daniel: Feedback session")),
            new Reminder(new DueDate("2025-11-14 1400"), new Description("Benson: Mock test practice")),
            new Reminder(new DueDate("2025-11-15 1600"), new Description("Carl: Science project discussion"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            samplePerson.addHomework(getSampleHomework()[4]);
            samplePerson.addHomework(getSampleHomework()[5]);
            samplePerson.getHomeworkList().get(1).markDone();

            for (int i = 1; i < 11; i++) {
                samplePerson.setPaymentStatus(i, true);
            }

            sampleAb.addPerson(samplePerson);

            int currentMonth = LocalDate.now().getMonth().getValue();
            if (!samplePerson.isPaidForMonth(currentMonth)) {
                sampleAb.addReminder(
                        UnmodifiablePaymentReminder.of(currentMonth, samplePerson, getMonthName(currentMonth)));
            }

            for (Reminder r : createHomeworkReminder(samplePerson)) {
                sampleAb.addReminder(r);
            }
        }
        for (Reminder sampleReminder : getSampleReminders()) {
            sampleAb.addReminder(sampleReminder);
        }
        return sampleAb;
    }

    /**
     * Returns a lesson time set containing the list of strings given.
     */
    public static Set<LessonTime> getLessonTimeSet(String... strings) {
        return Arrays.stream(strings)
                .map(LessonTime::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the month name corresponding to the given month number.
     */
    public static String getMonthName(int month) {
        String[] monthNames = { "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December" };
        return monthNames[month - 1];
    }
}
