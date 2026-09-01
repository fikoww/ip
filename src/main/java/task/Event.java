package puyo.task;

import java.time.LocalDateTime;

import puyo.parser.Parser;

/**
 * Represents an event task that occurs within a specific start and end time.
 */
public class Event extends Task {

    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Constructs an {@code Event} task with the specified description, start time, and end time.
     *
     * @param description The detailed description of the event.
     * @param start The starting date and time of the event.
     * @param end The ending date and time of the event.
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description, TaskType.EVENT);
        this.start = start;
        this.end = end;
    }

    /**
     * Formats the event task for storage file persistence.
     *
     * @return Formatted string representation for file storage.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + start.format(Parser.SAVE_DATETIME) + " | "
                + end.format(Parser.SAVE_DATETIME);
    }

    /**
     * Formats the event task for user interface display.
     *
     * @return Formatted string representation of the event task.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + start.format(Parser.DISPLAY_DATETIME) + " to: "
                + end.format(Parser.DISPLAY_DATETIME) + ")";
    }
}
