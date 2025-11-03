package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ParticipationHistoryReplaceSameDateTest {

    @Test
    public void addSameDate_replacesScore_newestRightmost() {
        ParticipationHistory h = new ParticipationHistory();

        h.add(LocalDate.parse("2025-09-10"), 1);
        h.add(LocalDate.parse("2025-09-11"), 2);
        h.add(LocalDate.parse("2025-09-12"), 3);
        h.add(LocalDate.parse("2025-09-13"), 4);
        h.add(LocalDate.parse("2025-09-14"), 5);

        // Replace the most recent date's score
        h.add(LocalDate.parse("2025-09-14"), 1);

        List<ParticipationRecord> five = h.asList(); // oldest -> newest
        assertEquals(5, five.size());

        // Ensure dates still strictly increasing (no duplicate 09-14 entries)
        assertEquals(LocalDate.parse("2025-09-10"), five.get(0).getDate());
        assertEquals(LocalDate.parse("2025-09-11"), five.get(1).getDate());
        assertEquals(LocalDate.parse("2025-09-12"), five.get(2).getDate());
        assertEquals(LocalDate.parse("2025-09-13"), five.get(3).getDate());
        assertEquals(LocalDate.parse("2025-09-14"), five.get(4).getDate());

        // Score for 2025-09-14 should be replaced
        assertEquals(1, five.get(4).getScore());

        // mostRecent points to the newest date
        ParticipationRecord recent = h.mostRecent();
        assertNotNull(recent);
        assertEquals(LocalDate.parse("2025-09-14"), recent.getDate());
        assertEquals(1, recent.getScore());
    }
}
