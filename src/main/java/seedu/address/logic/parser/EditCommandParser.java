package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.LessonTime;

/**
 * Parses input arguments and creates a new EditCommand object
 * <p>
 * This parser expects an index and at least 1 other prefix to be provided in the argument in the form:
 * {@code edit-student i/INDEX [n/NAME] [p/PHONE_NUMBER] [t/LESSON_TIME]...}, where {@code INDEX} refers to the
 * position of the student in the displayed list that is to be edited, and {@code NAME}, {@code PHONE_NUMBER},
 * and {@code LESSON_TIME} refer to the new values of the respective fields to be updated. Prefix {@code t/} will
 * replace existing set of lesson time to the new input {@code LESSON_TIME}.
 * <p>
 * Another form is {@code edit-student i/INDEX [n/NAME] [p/PHONE_NUMBER] [t+/LESSON_TIME]... [t-/LESSON_TIME]...}
 * where prefix {@code t+/} and {@code t-/} represents lesson time to add onto and remove from existing set of
 * lesson times respectively.
 */
public class EditCommandParser implements Parser<EditCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE, PREFIX_LESSON_TIME,
                        PREFIX_ADD_LESSON_TIME, PREFIX_DELETE_LESSON_TIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE);

        Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }

        boolean hasReplace = !argMultimap.getAllValues(PREFIX_LESSON_TIME).isEmpty();
        boolean hasAdd = !argMultimap.getAllValues(PREFIX_ADD_LESSON_TIME).isEmpty();
        boolean hasDelete = !argMultimap.getAllValues(PREFIX_DELETE_LESSON_TIME).isEmpty();

        if (hasReplace && (hasAdd || hasDelete)) {
            throw new ParseException(EditCommand.MESSAGE_MIXED_PREFIX);
        }

        if (hasReplace) {
            parseLessonTimeForEdit(argMultimap.getAllValues(PREFIX_LESSON_TIME))
                    .ifPresent(editPersonDescriptor::setLessonTime);
        }

        if (hasAdd) {
            parseLessonTimeForEdit(argMultimap.getAllValues(PREFIX_ADD_LESSON_TIME))
                    .ifPresent(editPersonDescriptor::setLessonTimesToAdd);
        }

        if (hasDelete) {
            parseLessonTimeForEdit(argMultimap.getAllValues(PREFIX_DELETE_LESSON_TIME))
                    .ifPresent(editPersonDescriptor::setLessonTimesToRemove);
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Returns {@code true} if all specified prefixes are present in the given {@link ArgumentMultimap},
     * meaning each prefix has a non-empty {@code Optional} value.
     *
     * @param argumentMultimap the mapping of prefixes to their arguments, obtained from tokenizing user input.
     * @param prefixes         the prefixes to check for presence.
     * @return {@code true} if all specified prefixes are present and non-empty; {@code false} otherwise.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses {@code Collection<String> lessonTime} into a {@code Set<LessonTime>} if {@code lessonTime} is non-empty.
     * If {@code lessonTime} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<LessonTime>} containing zero lesson time.
     */
    private Optional<Set<LessonTime>> parseLessonTimeForEdit(Collection<String> lessonTime) throws ParseException {
        assert lessonTime != null;

        if (lessonTime.isEmpty()) {
            return Optional.empty();
        }

        Collection<String> lessonTimeSet =
                lessonTime.size() == 1 && lessonTime.contains("") ? Collections.emptySet() : lessonTime;
        return Optional.of(ParserUtil.parseLessonTimeSet(lessonTimeSet));
    }
}
