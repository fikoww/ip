package puyo.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import puyo.PuyoException;
import puyo.command.AddCommand;
import puyo.command.ByeCommand;
import puyo.command.Command;
import puyo.command.DeleteCommand;
import puyo.command.ListCommand;
import puyo.command.MarkCommand;
import puyo.command.UnknownCommand;
import puyo.command.UnmarkCommand;
import puyo.task.Deadline;
import puyo.task.Event;
import puyo.task.ToDo;
import puyo.command.FindCommand;

/**
 * Parses user input strings into executable {@code Command} objects.
 */
public class Parser {

    /** Formatter for input dates in YYYY-MM-DD format. */
    public static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Formatter for input date and time in YYYY-MM-DD HHmm format. */
    public static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /** Formatter for displaying date and time in user interface. */
    public static final DateTimeFormatter DISPLAY_DATETIME = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /** Formatter for saving date and time into storage files. */
    public static final DateTimeFormatter SAVE_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses the raw user input into a corresponding {@code Command}.
     *
     * @param input The raw input string from the user.
     * @return The parsed {@code Command} object to be executed.
     * @throws PuyoException If the input format is invalid or empty.
     */
    public static Command parse(String input) throws PuyoException {
        if (input.isBlank()) {
            throw new PuyoException("Please enter a non-empty valid command!");
        }
        String firstWord = input.split(" ")[0].toLowerCase();
        switch (firstWord) {
            case "bye":
                return new ByeCommand();
            case "list":
                return new ListCommand();
            case "mark":
                return parseIndexCommand("mark", input, 5);
            case "unmark":
                return parseIndexCommand("unmark", input, 7);
            case "delete":
                return parseDeleteCommand(input);
            case "todo":
                return parseTodoCommand(input);
            case "deadline":
                return parseDeadlineCommand(input);
            case "event":
                return parseEventCommand(input);
            case "find":
                return parseFindCommand(input);
            default:
                return new UnknownCommand();
        }
    }

    /**
     * Parses commands that require a single task index (mark/unmark).
     *
     * @param type The command type string ("mark" or "unmark").
     * @param input The full user input string.
     * @param offset The character index offset to start parsing the task number.
     * @return The corresponding {@code MarkCommand} or {@code UnmarkCommand}.
     * @throws PuyoException If the index argument is invalid or missing.
     */
    private static Command parseIndexCommand(String type, String input, int offset) throws PuyoException {
        try {
            int index = Integer.parseInt(input.substring(offset).trim()) - 1;
            return type.equals("mark") ? new MarkCommand(index) : new UnmarkCommand(index);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new PuyoException("Please provide a valid task number! (e.g. " + type + " 1)");
        }
    }

    /**
     * Parses a delete command input string.
     *
     * @param input The full user input string.
     * @return A {@code DeleteCommand} containing the target index.
     * @throws PuyoException If the index argument is invalid or missing.
     */
    private static Command parseDeleteCommand(String input) throws PuyoException {
        try {
            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            return new DeleteCommand(index);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new PuyoException("Please provide a valid task number to delete! (e.g. delete 1)");
        }
    }

    /**
     * Parses a todo command input string.
     *
     * @param input The full user input string.
     * @return An {@code AddCommand} containing the created {@code ToDo} task.
     * @throws PuyoException If the description is empty.
     */
    private static Command parseTodoCommand(String input) throws PuyoException {
        String desc = input.substring(4).trim();
        if (desc.isEmpty()) {
            throw new PuyoException("The description of a todo can't be empty!");
        }
        return new AddCommand(new ToDo(desc));
    }

    /**
     * Parses a deadline command input string.
     *
     * @param input The full user input string.
     * @return An {@code AddCommand} containing the created {@code Deadline} task.
     * @throws PuyoException If the arguments or date format are invalid.
     */
    private static Command parseDeadlineCommand(String input) throws PuyoException {
        if (!input.toLowerCase().contains("/by")) {
            throw new PuyoException("Please enter a valid deadline by using '/by'!");
        }
        String[] parts = input.substring(9).split("/by", 2);
        String name = parts[0].trim();
        String byRaw = parts.length > 1 ? parts[1].trim() : "";
        if (name.isEmpty() || byRaw.isEmpty()) {
            throw new PuyoException("The description or time of a deadline can't be empty!");
        }
        LocalDateTime by = parseDateTime(byRaw);
        if (by == null) {
            throw new PuyoException("Invalid date format! Use: yyyy-MM-dd or yyyy-MM-dd HHmm (e.g. 2019-12-02 1800)");
        }
        return new AddCommand(new Deadline(name, by));
    }

    /**
     * Parses an event command input string.
     *
     * @param input The full user input string.
     * @return An {@code AddCommand} containing the created {@code Event} task.
     * @throws PuyoException If the arguments or date formats are invalid.
     */
    private static Command parseEventCommand(String input) throws PuyoException {
        String lower = input.toLowerCase();
        boolean hasFrom = lower.contains("/from");
        boolean hasTo = lower.contains("/to");
        if (!hasFrom && !hasTo) {
            throw new PuyoException("Please enter a valid event timing by using '/from' and '/to'!");
        } else if (!hasFrom) {
            throw new PuyoException("Please enter a valid starting event timing by using '/from'!");
        } else if (!hasTo) {
            throw new PuyoException("Please enter a valid ending event timing by using '/to'!");
        } else if (lower.indexOf("/from") > lower.indexOf("/to")) {
            throw new PuyoException("Please enter a valid event timing by putting '/from' before '/to'!");
        }
        String[] parts = input.substring(6).split("/from|/to");
        if (parts.length < 3) {
            throw new PuyoException("Event description, '/from', or '/to' cannot be empty!");
        }
        String name = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        if (name.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new PuyoException("Event description, '/from', or '/to' cannot be empty!");
        }
        LocalDateTime fromDT = parseDateTime(from);
        LocalDateTime toDT = parseDateTime(to);
        if (fromDT == null || toDT == null) {
            throw new PuyoException("Invalid date format! Use: yyyy-MM-dd or yyyy-MM-dd HHmm (e.g. 2019-12-02 1800)");
        }
        return new AddCommand(new Event(name, fromDT, toDT));
    }

    /**
     * Parses a date or date-time string into a {@code LocalDateTime} object.
     *
     * @param raw The raw date string.
     * @return Parsed {@code LocalDateTime} object, or {@code null} if parsing fails.
     */
    public static LocalDateTime parseDateTime(String raw) {
        raw = raw.trim();
        try {
            return LocalDateTime.parse(raw, INPUT_DATETIME);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(raw, INPUT_DATE).atStartOfDay();
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }

    /**
     * Parses a find command input string.
     *
     * @param input The full user input string.
     * @return A {@code FindCommand} with the specified search keyword.
     * @throws PuyoException If the search keyword is empty.
     */
    private static Command parseFindCommand(String input) throws PuyoException {
        String keyword = input.substring(4).trim();
        if (keyword.isEmpty()) {
            throw new PuyoException("The search keyword cannot be empty!");
        }
        return new FindCommand(keyword);
    }
}