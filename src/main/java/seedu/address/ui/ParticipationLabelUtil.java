package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.address.model.person.ParticipationRecord;

/** Builds the date labels shown above the participation boxes (no JavaFX). */
public final class ParticipationLabelUtil {
    private static final DateTimeFormatter MM_DD = DateTimeFormatter.ofPattern("MM-dd");
    private static final DateTimeFormatter YY = new DateTimeFormatterBuilder()
            .appendPattern("yy").toFormatter();

    private ParticipationLabelUtil() {}

    /**
     * From the five-slot participation list (may include nulls), return the label text per slot.
     * Rule: if any MM-dd repeats among non-null entries, append "\nYY" to all non-null labels.
     */
    public static List<String> formatLabels(List<ParticipationRecord> five) {
        Map<String, Integer> counts = new HashMap<>();
        for (ParticipationRecord r : five) {
            if (r != null) {
                String md = r.getDate().format(MM_DD);
                counts.put(md, counts.getOrDefault(md, 0) + 1);
            }
        }
        boolean ambiguous = counts.values().stream().anyMatch(c -> c > 1);

        List<String> out = new ArrayList<>(five.size());
        for (ParticipationRecord r : five) {
            if (r == null) {
                out.add("");
            } else {
                String md = r.getDate().format(MM_DD);
                out.add(ambiguous ? (md + "\n" + r.getDate().format(YY)) : md);
            }
        }
        return out;
    }
}
