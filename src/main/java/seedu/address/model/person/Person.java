package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.homework.Homework;


/**
 * Represents a Student in the student list.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    private static final String[] MONTH_NAMES = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private BitSet paymentStatus;
    private final ObservableList<Homework> homeworkList = FXCollections.observableArrayList();

    private final Set<LessonTime> lessonTime = new HashSet<>();

    // Participation (mutable history of last 5 records)
    private final ParticipationHistory participation = new ParticipationHistory();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Set<LessonTime> lessonTime) {
        requireAllNonNull(name, phone, lessonTime);
        this.name = name;
        this.phone = phone;
        this.lessonTime.addAll(lessonTime);
        this.paymentStatus = new BitSet(12);
    }

    /**
     * Every field must be present and not null.
     * Second constructor used by storage layer to reconstruct a person with existing payment data
     */
    public Person(Name name, Phone phone, Set<LessonTime> lessonTime, BitSet paymentStatus) {
        requireAllNonNull(name, phone, lessonTime, paymentStatus);
        this.name = name;
        this.phone = phone;
        this.lessonTime.addAll(lessonTime);
        this.paymentStatus = paymentStatus;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns an immutable lesson time set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<LessonTime> getLessonTime() {
        return Collections.unmodifiableSet(lessonTime);
    }

    public ObservableList<Homework> getHomeworkList() {
        return FXCollections.unmodifiableObservableList(homeworkList);
    }

    public void addHomework(Homework hw) {
        homeworkList.add(hw);
    }

    public void removeHomework(Homework hw) {
        homeworkList.remove(hw);
    }

    public void setHomeworkList(List<Homework> list) {
        homeworkList.setAll(list);
    }

    public ParticipationHistory getParticipation() {
        return participation;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && lessonTime.equals(otherPerson.lessonTime);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, lessonTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("lesson time", lessonTime)
                .toString();
    }

    /**
     * Returns the payment status for a specific month (1-12).
     */
    public boolean isPaidForMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return paymentStatus.get(month - 1);
    }

    /**
     * Replaces the current {@code paymentStatus} with the given {@code status}.
     */
    public void setAllPaymentStatus(BitSet status) {
        this.paymentStatus = status;
    }

    /**
     * Updates the payment status for a specific month.
     * Modifies the current person object instead of creating a new one.
     */
    public void setPaymentStatus(int month, boolean isPaid) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        paymentStatus.set(month - 1, isPaid);
    }

    /**
     * @return a string of payment status for each month
     */
    public String getPaymentStatusDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Payment Status:\n");
        for (int i = 0; i < 12; i++) {
            sb.append(MONTH_NAMES[i]).append(": ");
            if (paymentStatus.get(i)) {
                sb.append("✓ Paid");
            } else {
                sb.append("✗ Unpaid");
            }
            if (i < 11) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Creates a JavaFX VBox containing visual representation of payment status using Rectangles.
     * Displays month labels on top row and colored rectangles on bottom row (green for paid, red for unpaid).
     *
     * @return VBox containing payment status visualization
     */
    public HBox getPaymentStatusRectangles() {
        HBox container = new HBox(4);

        for (int i = 0; i < 12; i++) {
            VBox monthColumn = new VBox(3);
            monthColumn.setAlignment(Pos.CENTER);

            Text monthLabel = new Text(MONTH_NAMES[i]);
            monthLabel.setStyle("-fx-font-family: \"Segoe UI\"; -fx-font-size: 16px; -fx-fill: white;");

            Rectangle rect = new Rectangle(24, 24);
            if (paymentStatus.get(i)) {
                rect.setFill(Color.GREEN);
            } else {
                rect.setFill(Color.RED);
            }
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);

            monthColumn.getChildren().addAll(monthLabel, rect);
            container.getChildren().add(monthColumn);
        }

        return container;
    }

    public BitSet getPaymentStatusBitSet() {
        return (BitSet) paymentStatus.clone();
    }

    private void copyParticipationHistory(Person source, Person target) {
        for (ParticipationRecord record : source.participation.asList()) {
            target.participation.add(record);
        }
    }

    /**
     * Resets all payments if it's a new year
     */
    public void resetPaymentsIfNewYear() {
        java.time.LocalDate now = java.time.LocalDate.now();
        if (now.getMonthValue() == 1 && now.getDayOfMonth() == 1) {
            this.paymentStatus = new BitSet(12);
        }
    }
}
