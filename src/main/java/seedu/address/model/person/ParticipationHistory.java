package seedu.address.model.person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Stores up to 5 most recent participation records (chronological by date, oldest -> newest).
 * For a given date, only one record is kept; adding the same date replaces the score.
 */
public class ParticipationHistory {
    private static final int MAX = 5;

    // Sorted by date; unique key per day.
    private final NavigableMap<LocalDate, ParticipationRecord> byDate = new TreeMap<>();

    public ParticipationHistory() {}

    /**
     * Creates a participation history pre-populated with the given records.
     * If more than 5 unique dates are given, only the 5 most recent dates are kept.
     * If there are multiple records with the same date, the last one seen wins.
     */
    public ParticipationHistory(List<ParticipationRecord> records) {
        if (records != null) {
            for (ParticipationRecord r : records) {
                add(r);
            }
        }
    }

    /** Adds/replaces by date, keeping only the 5 most recent dates. */
    public void add(ParticipationRecord record) {
        if (record == null) {
            return; // ignore nulls to match existing test expectations
        }
        byDate.put(record.getDate(), record); // replace if same date
        // cap to last 5 dates (most recent = greatest key)
        while (byDate.size() > MAX) {
            byDate.pollFirstEntry(); // drop oldest date
        }
    }

    public void add(LocalDate date, int score) {
        add(new ParticipationRecord(date, score));
    }

    /** Oldest -> newest, immutable list. */
    public List<ParticipationRecord> asList() {
        return Collections.unmodifiableList(new ArrayList<>(byDate.values()));
    }

    /** Oldest -> newest, padded to 5 with nulls at the oldest side. */
    public List<ParticipationRecord> asListPaddedToFive() {
        List<ParticipationRecord> raw = new ArrayList<>(byDate.values()); // oldest -> newest
        int missing = MAX - raw.size();
        List<ParticipationRecord> padded = new ArrayList<>(MAX);
        for (int i = 0; i < missing; i++) {
            padded.add(null);
        }
        padded.addAll(raw);
        return Collections.unmodifiableList(padded);
    }

    public int size() {
        return byDate.size();
    }

    /** Most recent (newest date), or {@code null} if empty. */
    public ParticipationRecord mostRecent() {
        return byDate.isEmpty() ? null : byDate.lastEntry().getValue();
    }
}
