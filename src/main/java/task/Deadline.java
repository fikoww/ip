package puyo.task;

import puyo.parser.Parser;
import java.time.LocalDateTime;

public class Deadline extends Task {

    LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + by.format(Parser.SAVE_DATETIME);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(Parser.DISPLAY_DATETIME) + ")";
    }
}