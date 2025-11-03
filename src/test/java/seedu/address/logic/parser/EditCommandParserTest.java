package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LESSON_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LESSON_TIME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LESSON_TIME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LESSON_TIME_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LESSON_TIME_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.LessonTime;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, " " + PREFIX_INDEX + "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, " " + PREFIX_INDEX + "-5" + NAME_DESC_AMY, ParserUtil.MESSAGE_INVALID_INDEX);

        // zero index
        assertParseFailure(parser, " " + PREFIX_INDEX + "0" + NAME_DESC_AMY, ParserUtil.MESSAGE_INVALID_INDEX);

        // invalid arguments being parsed as index
        assertParseFailure(parser, " " + PREFIX_INDEX + "1 some random string", ParserUtil.MESSAGE_INVALID_INDEX);

        // invalid prefix being parsed as index
        assertParseFailure(parser, " index/1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, " " + PREFIX_INDEX + "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, " " + PREFIX_INDEX + "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // invalid lesson time
        assertParseFailure(parser, " " + PREFIX_INDEX + "1" + INVALID_LESSON_TIME_DESC,
                LessonTime.MESSAGE_CONSTRAINTS);

        // invalid phone followed by valid lesson time
        assertParseFailure(parser, " " + PREFIX_INDEX + "1" + INVALID_PHONE_DESC + LESSON_TIME_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, " " + PREFIX_INDEX + "1" + INVALID_NAME_DESC + INVALID_PHONE_DESC
                + LESSON_TIME_DESC_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        // l/ prefix
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + NAME_DESC_AMY + PHONE_DESC_BOB
                + LESSON_TIME_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withLessonTime(VALID_LESSON_TIME_1, VALID_LESSON_TIME_2).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);

        // combi of l+/ and l-/ prefix
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + NAME_DESC_AMY + PHONE_DESC_BOB
                + " " + PREFIX_ADD_LESSON_TIME + VALID_LESSON_TIME_1 + " " + PREFIX_DELETE_LESSON_TIME
                + VALID_LESSON_TIME_2;
        descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_BOB)
                .withLessonTimeToAdd(VALID_LESSON_TIME_1).withLessonTimeToRemove(VALID_LESSON_TIME_2).build();

        expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + PHONE_DESC_BOB + LESSON_TIME_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withLessonTime(VALID_LESSON_TIME_1, VALID_LESSON_TIME_2).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_combiOfAddRemoveLessonTime_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + " " + PREFIX_ADD_LESSON_TIME
                + VALID_LESSON_TIME_1 + " " + PREFIX_DELETE_LESSON_TIME + VALID_LESSON_TIME_2;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withLessonTimeToAdd(VALID_LESSON_TIME_1)
                .withLessonTimeToRemove(VALID_LESSON_TIME_2).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // lesson time to replace all
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + LESSON_TIME_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withLessonTime(VALID_LESSON_TIME_1, VALID_LESSON_TIME_2).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // lesson time to add
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + " " + PREFIX_ADD_LESSON_TIME
                + VALID_LESSON_TIME_1;
        descriptor = new EditPersonDescriptorBuilder().withLessonTimeToAdd(VALID_LESSON_TIME_1).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // lesson time to delete
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + " " + PREFIX_DELETE_LESSON_TIME
                + VALID_LESSON_TIME_1;
        descriptor = new EditPersonDescriptorBuilder().withLessonTimeToRemove(VALID_LESSON_TIME_1).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonLessonTimeValue_failure()

        // invalid followed by valid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // valid followed by invalid
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + PHONE_DESC_AMY + LESSON_TIME_DESC_AMY
                + PHONE_DESC_AMY + LESSON_TIME_DESC_BOB + PHONE_DESC_BOB + LESSON_TIME_DESC_BOB;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple invalid values
        userInput = " " + PREFIX_INDEX + targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_LESSON_TIME_DESC
                + INVALID_PHONE_DESC + INVALID_LESSON_TIME_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
    }

    @Test
    public void parse_mixedPrefixUsed_throwsParseException() {
        // all 3 prefix used
        String threePrefixInput = " " + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased() + LESSON_TIME_DESC_AMY + " "
                + PREFIX_ADD_LESSON_TIME + "1530 Fri " + PREFIX_DELETE_LESSON_TIME + "1800 Tue";

        assertThrows(ParseException.class, () -> parser.parse(threePrefixInput));

        // l/ together with l+/
        String twoPrefixInput = " " + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased() + LESSON_TIME_DESC_AMY + " "
                + PREFIX_ADD_LESSON_TIME + "1530 Fri";

        assertThrows(ParseException.class, () -> parser.parse(twoPrefixInput));

        // l/ together with l-/
        String userInput = " " + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased() + LESSON_TIME_DESC_AMY + " "
                + PREFIX_DELETE_LESSON_TIME + "1800 Tue";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
