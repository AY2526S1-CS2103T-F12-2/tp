package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ParticipationHistoryOrderingTest {

    @Test
    public void addOutOfOrder_keepsChronological_dropsOldest() {
        ParticipationHistory h = new ParticipationHistory();

        // Add in arbitrary order
        h.add(LocalDate.parse("2025-09-14"), 5);
        h.add(LocalDate.parse("2025-09-10"), 1);
        h.add(LocalDate.parse("2025-09-12"), 3);
        h.add(LocalDate.parse("2025-09-11"), 2);
        h.add(LocalDate.parse("2025-09-13"), 4);

        // Now exceed cap => oldest (2025-09-10) should be dropped
        h.add(LocalDate.parse("2025-09-15"), 1);

        List<ParticipationRecord> five = h.asList(); // oldest -> newest
        assertEquals(5, five.size());
        assertEquals(LocalDate.parse("2025-09-11"), five.get(0).getDate());
        assertEquals(LocalDate.parse("2025-09-12"), five.get(1).getDate());
        assertEquals(LocalDate.parse("2025-09-13"), five.get(2).getDate());
        assertEquals(LocalDate.parse("2025-09-14"), five.get(3).getDate());
        assertEquals(LocalDate.parse("2025-09-15"), five.get(4).getDate());
    }
}
