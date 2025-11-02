package seedu.address.testutil;

import java.time.LocalDate;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.LessonTime;
import seedu.address.model.person.Name;
import seedu.address.model.person.ParticipationHistory;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_LESSON_TIME = "1000 Sat";

    private Name name;
    private Phone phone;
    private Set<LessonTime> lessonTime = new HashSet<>();
    private BitSet paymentStatus = new BitSet(12);
    private ParticipationHistory participation = new ParticipationHistory();

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        lessonTime.add(new LessonTime(DEFAULT_LESSON_TIME));
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        lessonTime = personToCopy.getLessonTime();
        paymentStatus = (BitSet) personToCopy.getPaymentStatusBitSet().clone();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code LessonTime} of the {@code Person} that we are building.
     */
    public PersonBuilder withLessonTime(String... lessonTime) {
        this.lessonTime = SampleDataUtil.getLessonTimeSet(lessonTime);
        return this;
    }

    /**
     * Sets the {@code PaymentStatus} of the {@code Person} that we are building.
     */
    public PersonBuilder withPaymentStatus(String pattern) {
        BitSet bits = new BitSet(12);
        for (int i = 0; i < Math.min(12, pattern.length()); i++) {
            if (pattern.charAt(i) == '1') {
                bits.set(i);
            }
        }
        this.paymentStatus = bits;
        return this;
    }

    /**
     * Sets the {@code Participation} of the {@code Person} that we are building.
     */
    public PersonBuilder withParticipation(String date, int score) {
        participation.add(LocalDate.parse(date), score);
        return this;
    }

    public Person build() {
        return new Person(name, phone, lessonTime);
    }
}
