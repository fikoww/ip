package puyo.task;

import java.time.LocalDateTime;

import puyo.parser.Parser;

/**
 * Represents a task that needs to be completed before a specific deadline.
 */
public class Deadline extends Task {

    private final LocalDateTime by;

    /**
     * Constructs a {@code Deadline} task with the specified description and due date/time.
     *
     * @param description The detailed description of the task.
     * @param by The deadline date and time.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Formats the deadline task for storage file persistence.
     *
     * @return Formatted string representation for file storage.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + by.format(Parser.SAVE_DATETIME);
    }

    /**
     * Formats the deadline task for user interface display.
     *
     * @return Formatted string representation of the deadline task.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(Parser.DISPLAY_DATETIME) + ")";
    }
}