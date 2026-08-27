package puyo.parser;

import org.junit.jupiter.api.Test;
import puyo.PuyoException;
import puyo.command.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    // -------------------------------------------------------------------------
    // 1. Basic & Simple Commands
    // -------------------------------------------------------------------------

    @Test
    public void parse_emptyInput_exceptionThrown() {
        PuyoException ex = assertThrows(PuyoException.class, () -> Parser.parse("   "));
        assertEquals("Please enter a non-empty valid command!", ex.getMessage());
    }

    @Test
    public void parse_byeCommand_returnsByeCommand() throws Exception {
        Command c = Parser.parse("bye");
        assertInstanceOf(ByeCommand.class, c);
    }

    @Test
    public void parse_listCommand_returnsListCommand() throws Exception {
        Command c = Parser.parse("list");
        assertInstanceOf(ListCommand.class, c);
    }

    @Test
    public void parse_unknownCommand_returnsUnknownCommand() throws Exception {
        Command c = Parser.parse("foobar 123");
        assertInstanceOf(UnknownCommand.class, c);
    }

    // -------------------------------------------------------------------------
    // 2. Mark / Unmark / Delete Commands
    // -------------------------------------------------------------------------

    @Test
    public void parse_markValidIndex_returnsMarkCommand() throws Exception {
        Command c = Parser.parse("mark 2");
        assertInstanceOf(MarkCommand.class, c);
    }

    @Test
    public void parse_markInvalidIndex_exceptionThrown() {
        assertThrows(PuyoException.class, () -> Parser.parse("mark abc"));
        assertThrows(PuyoException.class, () -> Parser.parse("mark"));
    }

    @Test
    public void parse_unmarkValidIndex_returnsUnmarkCommand() throws Exception {
        Command c = Parser.parse("unmark 5");
        assertInstanceOf(UnmarkCommand.class, c);
    }

    @Test
    public void parse_deleteValidIndex_returnsDeleteCommand() throws Exception {
        Command c = Parser.parse("delete 1");
        assertInstanceOf(DeleteCommand.class, c);
    }

    @Test
    public void parse_deleteMissingIndex_exceptionThrown() {
        assertThrows(PuyoException.class, () -> Parser.parse("delete "));
    }

    // -------------------------------------------------------------------------
    // 3. ToDo Commands
    // -------------------------------------------------------------------------

    @Test
    public void parse_todoValidDescription_returnsAddCommand() throws Exception {
        Command c = Parser.parse("todo read book");
        assertInstanceOf(AddCommand.class, c);
    }

    @Test
    public void parse_todoEmptyDescription_exceptionThrown() {
        PuyoException ex = assertThrows(PuyoException.class, () -> Parser.parse("todo "));
        assertEquals("The description of a todo can't be empty!", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // 4. Deadline Commands
    // -------------------------------------------------------------------------

    @Test
    public void parse_deadlineValidDateTime_returnsAddCommand() throws Exception {
        Command c = Parser.parse("deadline return book /by 2026-12-02 1800");
        assertInstanceOf(AddCommand.class, c);
    }

    @Test
    public void parse_deadlineMissingByFlag_exceptionThrown() {
        PuyoException ex = assertThrows(PuyoException.class, () -> Parser.parse("deadline return book 2026-12-02"));
        assertEquals("Please enter a valid deadline by using '/by'!", ex.getMessage());
    }

    @Test
    public void parse_deadlineInvalidDateFormat_exceptionThrown() {
        assertThrows(PuyoException.class, () -> Parser.parse("deadline return book /by 02-12-2026"));
    }

    // -------------------------------------------------------------------------
    // 5. Event Commands
    // -------------------------------------------------------------------------

    @Test
    public void parse_eventValidDateTime_returnsAddCommand() throws Exception {
        Command c = Parser.parse("event project meeting /from 2026-08-28 1400 /to 2026-08-28 1600");
        assertInstanceOf(AddCommand.class, c);
    }

    @Test
    public void parse_eventReversedFlags_exceptionThrown() {
        PuyoException ex = assertThrows(PuyoException.class, () ->
                Parser.parse("event project meeting /to 2026-08-28 1600 /from 2026-08-28 1400")
        );
        assertEquals("Please enter a valid event timing by putting '/from' before '/to'!", ex.getMessage());
    }

    @Test
    public void parse_eventMissingToFlag_exceptionThrown() {
        assertThrows(PuyoException.class, () -> Parser.parse("event project meeting /from 2026-08-28 1400"));
    }

    // -------------------------------------------------------------------------
    // 6. DateTime Parsing Helper
    // -------------------------------------------------------------------------

    @Test
    public void parseDateTime_validInputs_returnsLocalDateTime() {
        LocalDateTime dt1 = Parser.parseDateTime("2026-08-28 1800");
        assertNotNull(dt1);
        assertEquals(18, dt1.getHour());

        LocalDateTime dt2 = Parser.parseDateTime("2026-08-28");
        assertNotNull(dt2);
        assertEquals(0, dt2.getHour());
    }

    @Test
    public void parseDateTime_invalidInput_returnsNull() {
        assertNull(Parser.parseDateTime("28-08-2026"));
        assertNull(Parser.parseDateTime("invalid date"));
    }
}