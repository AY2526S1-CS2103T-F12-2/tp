package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.LessonTime;
import seedu.address.model.person.ParticipationHistory;
import seedu.address.model.person.ParticipationRecord;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {
    private static final String FXML = "PersonListCard.fxml";
    private static final DateTimeFormatter MM_DD = DateTimeFormatter.ofPattern("MM-dd");

    /**
     * The person shown by this card.
     */
    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private FlowPane lessonTime;
    @FXML
    private FlowPane groupBadges;
    @FXML
    private AnchorPane homeworkPlaceholder;
    @FXML
    private VBox paymentStatusContainer;

    // Participation UI
    @FXML
    private HBox dateRow;
    @FXML
    private HBox boxes;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        person.getLessonTime().stream()
                .sorted(Comparator.comparing((LessonTime lt) -> lt.day).thenComparing(lt -> lt.time))
                .forEach(lt -> lessonTime.getChildren().add(new Label(lt.toString())));
        HomeworkListPanel panel = new HomeworkListPanel(person.getHomeworkList());
        homeworkPlaceholder.getChildren().setAll(panel.getRoot());
        renderGroupBadges(person);
        renderParticipation(person.getParticipation());
        renderPaymentStatus(person);
    }

    private void renderGroupBadges(Person p) {
        if (groupBadges == null) {
            return;
        }
        groupBadges.getChildren().clear();
        try {
            Set<GroupName> groups = UiGroupAccess.groupsOf(p);
            for (GroupName g : groups) {
                Label chip = new Label(g.toString());
                chip.getStyleClass().add("group-badge");
                chip.setMinHeight(Region.USE_PREF_SIZE); // prevent vertical compression of rounded chips
                groupBadges.getChildren().add(chip);
            }
        } catch (Throwable ignored) {
            // If bridge not installed, just show nothing
        }
    }

    private void renderParticipation(ParticipationHistory history) {
        if (boxes == null || dateRow == null || history == null) {
            return; // FXML fields not present or no history yet
        }

        boxes.getChildren().clear();
        dateRow.getChildren().clear();

        // Oldest -> newest, padded to 5 (nulls for missing oldest entries)
        List<ParticipationRecord> five = history.asListPaddedToFive();

        // Build the label strings (pure logic, unit-testable)
        List<String> labels = ParticipationLabelUtil.formatLabels(five);

        for (int i = 0; i < five.size(); i++) {
            ParticipationRecord r = five.get(i);

            // ----- date label (top row) -----
            Label d = new Label(labels.get(i));
            d.getStyleClass().add("date-mini");
            d.setMinWidth(44);
            d.setPrefWidth(44);
            d.setMaxWidth(Region.USE_PREF_SIZE);
            d.setAlignment(Pos.CENTER);
            d.setWrapText(true);
            if (r != null) {
                Tooltip.install(d, new Tooltip(r.getDate().toString())); // ISO tooltip
            }
            dateRow.getChildren().add(d);

            // ----- score box (bottom row) -----
            StackPane cell = new StackPane();
            cell.setMinWidth(44);
            cell.setPrefWidth(44);
            cell.setMaxWidth(Region.USE_PREF_SIZE);
            cell.setAlignment(Pos.CENTER);

            Rectangle rect = new Rectangle(24, 24);
            rect.getStyleClass().add("participation-box");

            Text t = new Text(r == null ? "" : Integer.toString(r.getScore()));
            cell.getChildren().addAll(rect, t);
            boxes.getChildren().add(cell);
        }

    }

    /**
     * Renders the payment status using JavaFX Rectangles.
     */
    private void renderPaymentStatus(Person person) {
        if (paymentStatusContainer == null) {
            return;
        }
        paymentStatusContainer.getChildren().clear();

        Label title = new Label("Payment Status:");
        title.getStyleClass().add("payment-status-title");
        paymentStatusContainer.getChildren().add(title);

        HBox rectangles = person.getPaymentStatusRectangles();
        paymentStatusContainer.getChildren().add(rectangles);
    }
}
