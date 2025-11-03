package seedu.address.logic.parser.homeworkparsers;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.time.LocalDate;
import java.util.stream.Stream;

import seedu.address.logic.commands.homeworkcommands.AddHomeworkCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.homework.Homework;
import seedu.address.model.person.Name;


/**
 * Parses input arguments and creates a new {@code AddHomeworkCommand} object.
 * <p>
 * This parser expects three prefixes to be provided in the argument in the form:
 * {@code add-homework n/NAME d/DESCRIPTION by/DEADLINE}, where
 * {@code NAME} refers to the student's name,
 * {@code DESCRIPTION} refers to the homework description,
 * and {@code DEADLINE} refers to the due date of the homework in {@code yyyy-MM-dd} format.
 */

public class AddHomeworkCommandParser implements Parser<AddHomeworkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code AddHomeworkCommand}
     * and returns an AddHomeworkCommand object for execution.
     *
     * @param args Full user input string containing the command arguments.
     * @return An {@code AddHomeworkCommand} object that is ready to be executed.
     * @throws ParseException If the user input does not conform to the expected format,
     *
     */
    public AddHomeworkCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESC, PREFIX_DEADLINE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_DESC, PREFIX_DEADLINE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE));
        }

        arePrefixesValid(argMultimap, PREFIX_NAME, PREFIX_DESC, PREFIX_DEADLINE);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        String description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESC).get());
        LocalDate deadline = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DEADLINE).get());

        if (description.length() > 50) {
            throw new ParseException(AddHomeworkCommand.MESSAGE_DESC_TOO_LONG);
        }

        Homework hw = new Homework(description, deadline);
        return new AddHomeworkCommand(name, hw);
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
     * Checks that each given prefix appears exactly once
     * throws a {@link ParseException} if a prefix appears more than once.
     *
     * @param map The argument map containing prefixes and their values.
     * @param prefixes The prefixes to check.
     * @return {@code true} if all prefixes are present exactly once; {@code false} if any are missing.
     * @throws ParseException If any prefix appears more than once.
     */
    private static boolean arePrefixesValid(ArgumentMultimap map, Prefix... prefixes) throws ParseException {
        for (Prefix prefix : prefixes) {
            var values = map.getAllValues(prefix);
            if (values.size() > 1) {
                throw new ParseException("Multiple values specified for prefix: " + prefix.getPrefix());
            }
        }
        return true;
    }

}


