package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ParticipationHistoryCappingTest {

    @Test
    public void addSixUniqueDates_keepsNewestFive() {
        ParticipationHistory h = new ParticipationHistory();

        // Oldest
        h.add(LocalDate.parse("2024-10-31"), 2);
        h.add(LocalDate.parse("2025-01-01"), 1);
        h.add(LocalDate.parse("2025-07-25"), 3);
        h.add(LocalDate.parse("2025-08-19"), 4);
        h.add(LocalDate.parse("2025-10-30"), 5);
        // Newest (6th) -> should drop 2024-10-31, keep 2025-10-31
        h.add(LocalDate.parse("2025-10-31"), 4);

        List<ParticipationRecord> five = h.asList(); // oldest -> newest
        assertEquals(5, five.size());

        // Confirm oldest is now 2025-01-01 (2024-10-31 was dropped)
        assertEquals(LocalDate.parse("2025-01-01"), five.get(0).getDate());
        // Confirm newest is 2025-10-31
        assertEquals(LocalDate.parse("2025-10-31"), five.get(4).getDate());
    }

    @Test
    public void addSameDate_replacesScoreKeepsOrder() {
        ParticipationHistory h = new ParticipationHistory();
        h.add(LocalDate.parse("2025-10-30"), 1);
        h.add(LocalDate.parse("2025-10-31"), 2);
        // replace score on same date
        h.add(LocalDate.parse("2025-10-31"), 5);

        List<ParticipationRecord> list = h.asList();
        assertEquals(2, list.size());
        assertEquals(LocalDate.parse("2025-10-30"), list.get(0).getDate());
        assertEquals(LocalDate.parse("2025-10-31"), list.get(1).getDate());
        assertEquals(5, list.get(1).getScore()); // replaced
    }
}
