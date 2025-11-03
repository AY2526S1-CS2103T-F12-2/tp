package seedu.address.model.person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Stores up to 5 most recent participation records (ordered by date, oldest -> newest).
 * <p>
 * Semantics:
 * - Adding a record with a date that already exists replaces the score for that date.
 * - The structure always keeps at most 5 unique dates: the five most recent by date.
 * - Iteration/order APIs return oldest -> newest (so UI rightmost = newest).
 */
public class ParticipationHistory {
    private static final int MAX = 5;

    /** Sorted by date (natural order), so firstKey() is the oldest and lastKey() is the newest. */
    private final NavigableMap<LocalDate, ParticipationRecord> byDate = new TreeMap<>();

    public ParticipationHistory() {}

    /**
     * Creates a participation history pre-populated with the given records.
     * Null entries are ignored. If more than 5 unique dates are present,
     * only the five most recent (by date) are kept.
     */
    public ParticipationHistory(List<ParticipationRecord> records) {
        if (records != null) {
            for (ParticipationRecord r : records) {
                add(r);
            }
        }
    }

    /**
     * Add or replace a record.
     * - If {@code record} is null, it is ignored.
     * - If the date already exists, its score is replaced by the new one.
     * - If size exceeds 5 after insertion/replacement, the oldest date is dropped.
     */
    public void add(ParticipationRecord record) {
        if (record == null) {
            return;
        }
        LocalDate date = Objects.requireNonNull(record.getDate(), "date");
        // Replace-or-insert by date:
        byDate.put(date, record);

        // Enforce cap by dropping the oldest date if necessary.
        while (byDate.size() > MAX) {
            byDate.pollFirstEntry();
        }
    }

    public void add(LocalDate date, int score) {
        add(new ParticipationRecord(date, score));
    }

    /** Returns an immutable list (oldest -> newest) of up to 5 records. */
    public List<ParticipationRecord> asList() {
        return Collections.unmodifiableList(new ArrayList<>(byDate.values()));
    }

    /**
     * Returns a list padded to 5 entries, with nulls for the missing oldest entries.
     * Oldest entries come first, newest last, so UI can render left->right.
     */
    public List<ParticipationRecord> asListPaddedToFive() {
        List<ParticipationRecord> raw = new ArrayList<>(byDate.values()); // oldest -> newest
        int missing = MAX - raw.size();
        List<ParticipationRecord> padded = new ArrayList<>(missing + raw.size());
        for (int i = 0; i < missing; i++) {
            padded.add(null);
        }
        padded.addAll(raw);
        return Collections.unmodifiableList(padded);
    }

    public int size() {
        return byDate.size();
    }

    public ParticipationRecord mostRecent() {
        return byDate.isEmpty() ? null : byDate.lastEntry().getValue();
    }
}
