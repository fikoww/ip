package puyo.task;

import puyo.parser.Parser;
import java.time.LocalDateTime;

public class Event extends Task {

    LocalDateTime start;
    LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description, TaskType.EVENT);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + start.format(Parser.SAVE_DATETIME) + " | " + end.format(Parser.SAVE_DATETIME);
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + start.format(Parser.DISPLAY_DATETIME) + " to: " + end.format(Parser.DISPLAY_DATETIME) + ")";
    }
}