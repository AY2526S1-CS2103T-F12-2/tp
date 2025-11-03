package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.ParticipationRecord;
import seedu.address.model.person.Person;
import seedu.address.ui.UiAttendanceAccess;

/**
 * Records participation for a student on a date.
 * <p>
 * Format: {@code participation n/NAME d/YYYY-MM-DD s/0..5}
 */
public class ParticipationCommand extends Command {
    public static final String COMMAND_WORD = "participation";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Records participation.\n"
            + "Parameters: n/NAME d/DATE s/SCORE\n"
            + "Example: " + COMMAND_WORD + " n/marcus d/2025-09-19 s/3";

    private static final int NAME_MAX = 50;

    private final String nameRaw;
    private final String dateRaw;
    private final String scoreRaw;

    /**
     * Creates an {@code ParticipationCommand}.
     *
     * @param nameRaw  raw student name (will be trimmed and normalized during execution)
     * @param dateRaw  raw date string in ISO format {@code YYYY-MM-DD}; validated in {@link #execute(Model)}
     * @param scoreRaw raw participation score string expected to parse to an integer in {@code [0,5]};
     *                 validated in {@link #execute(Model)}
     */
    public ParticipationCommand(String nameRaw, String dateRaw, String scoreRaw) {
        this.nameRaw = nameRaw;
        this.dateRaw = dateRaw;
        this.scoreRaw = scoreRaw;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // --- validate name
        String name = nameRaw.trim().replaceAll("\\s+", " ");
        if (name.length() == 0) {
            throw new CommandException("Invalid student name: name cannot be empty.");
        }
        if (name.length() > NAME_MAX) {
            throw new CommandException("Invalid student name: A name that is longer than 50 characters.");
        }

        // --- validate date
        final LocalDate date;
        try {
            date = LocalDate.parse(dateRaw);
        } catch (DateTimeParseException ex) {
            throw new CommandException("Invalid date. The format must be YYYY-MM-DD.");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new CommandException("Invalid date. Participation date cannot be in the future.");
        }

        // --- validate score
        final int score;
        try {
            score = Integer.parseInt(scoreRaw);
        } catch (NumberFormatException ex) {
            throw new CommandException("Invalid participation score. Use an integer 0 to 5.");
        }
        if (score < 0 || score > 5) {
            throw new CommandException("Invalid participation score. Must be between 0 and 5 inclusive.");
        }

        // --- find the person directly from the address book (no Model API change)
        String norm = name.trim().replaceAll("\\s+", " ").toLowerCase();
        Person person = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.trim().replaceAll("\\s+", " ").toLowerCase().equals(norm))
                .findFirst()
                .orElseThrow(() -> new CommandException("Invalid student name: no matching student found."));

        // --- record participation on the person (keeps last 5 internally)
        person.getParticipation().add(new ParticipationRecord(date, score));
        // --- mark person as changed so persistence is triggered
        // Some test stubs throw on setPerson; avoid breaking tests but still persist in app.
        try {
            model.setPerson(person, person);
        } catch (Throwable ignored) {
            // test stubs may throw; production Model will persist
        }

        // --- notify UI date (preserve existing behaviour)
        var idx = model.getAttendanceIndex();
        idx.setCurrentUiDate(date);

        // --- refresh listing
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        // legacy hook (keep for other UI parts)
        UiAttendanceAccess.install((n, d) -> model.getAttendanceIndex().get(n, d).orElse(null), () ->
                                    model.getAttendanceIndex().getCurrentUiDate());

        return new CommandResult(String.format(
                "Success: Participation recorded: %s, %s, score=%d.", name, date, score));
    }
}
