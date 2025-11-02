package seedu.address.logic.parser.homeworkparsertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.homeworkcommands.AddHomeworkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.homeworkparsers.AddHomeworkCommandParser;
import seedu.address.model.homework.Homework;
import seedu.address.model.person.Name;

public class AddHomeworkParserTest {

    private AddHomeworkCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new AddHomeworkCommandParser();
    }


    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        String input = " n/Alex Yeoh desc/Math Homework by/2025-10-27";
        AddHomeworkCommand expected = new AddHomeworkCommand(
                new Name("Alex Yeoh"),
                new Homework("Math Homework", LocalDate.parse("2025-10-27")));

        AddHomeworkCommand actual = parser.parse(input);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_orderIrrelevant_success() throws Exception {
        String input = " by/2025-12-01 desc/Science WS n/Bernice Yu";
        AddHomeworkCommand expected = new AddHomeworkCommand(
                new Name("Bernice Yu"),
                new Homework("Science WS", LocalDate.parse("2025-12-01")));

        assertEquals(expected, parser.parse(input));
    }

    @Test
    public void parse_descriptionWhitespaceCollapsed_success() throws Exception {
        String input = " n/Alex Yeoh desc/  Math\t  Homework   1   by/2025-10-27";
        AddHomeworkCommand expected = new AddHomeworkCommand(
                new Name("Alex Yeoh"),
                new Homework("Math Homework 1", LocalDate.parse("2025-10-27")));

        assertEquals(expected, parser.parse(input));
    }

    @Test
    public void parse_descriptionTooLong_throwsParseException() {
        String longDesc = "a".repeat(51); // 51 characters
        String input = " n/Alex Yeoh desc/" + longDesc + " by/2025-10-27";

        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(AddHomeworkCommand.MESSAGE_DESC_TOO_LONG, ex.getMessage());
    }




    @Test
    public void parse_duplicateNamePrefix_throwsParseException() {
        String input = " n/Alex Yeoh desc/Math by/2025-10-27 n/Bernice Yu";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_duplicateDescPrefix_throwsParseException() {
        String input = " n/Alex Yeoh desc/Math desc/Science by/2025-10-27";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_duplicateDeadlinePrefix_throwsParseException() {
        String input = " n/Alex Yeoh desc/Math by/2025-10-27 by/2025-09-09";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }


    @Test
    public void parse_missingNamePrefix_failure() {
        assertInvalidFormat(" desc/Math by/2025-10-27");
    }

    @Test
    public void parse_missingDescPrefix_failure() {
        assertInvalidFormat(" n/Alex Yeoh by/2025-10-27");
    }

    @Test
    public void parse_missingDeadlinePrefix_failure() {
        assertInvalidFormat(" n/Alex Yeoh desc/Math");
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertInvalidFormat(" randomText n/Alex desc/Math by/2025-10-27");
    }


    private void assertInvalidFormat(String input) {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE), ex.getMessage());
    }
}
