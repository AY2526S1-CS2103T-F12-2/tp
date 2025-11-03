package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.ParticipationRecord;

public class ParticipationLabelUtilTest {

    @Test
    public void formatLabels_nonAmbiguous_singleLine() {
        List<ParticipationRecord> five = Arrays.asList(
                new ParticipationRecord(LocalDate.parse("2025-09-10"), 2),
                new ParticipationRecord(LocalDate.parse("2025-09-11"), 3),
                null,
                null,
                new ParticipationRecord(LocalDate.parse("2025-10-05"), 1)
        );
        List<String> labels = ParticipationLabelUtil.formatLabels(five);
        assertEquals("09-10", labels.get(0));
        assertEquals("09-11", labels.get(1));
        assertEquals("", labels.get(2));
        assertEquals("", labels.get(3));
        assertEquals("10-05", labels.get(4));
    }

    @Test
    public void formatLabels_ambiguous_appendYearSecondLine() {
        List<ParticipationRecord> five = Arrays.asList(
                new ParticipationRecord(LocalDate.parse("2024-10-31"), 1),
                new ParticipationRecord(LocalDate.parse("2025-10-31"), 1),
                null,
                null,
                null
        );
        List<String> labels = ParticipationLabelUtil.formatLabels(five);
        assertEquals("10-31\n24", labels.get(0));
        assertEquals("10-31\n25", labels.get(1));
        assertEquals("", labels.get(2));
        assertEquals("", labels.get(3));
        assertEquals("", labels.get(4));
    }
}
