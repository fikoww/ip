package puyo.task;

public abstract class Task {

    private String name;
    private boolean done;
    private TaskType type;

    Task(String name, TaskType type) {
        this.name = name.trim();
        this.done = false;
        this.type = type;
    }

    public void markDone() {
        this.done = true;
    }

    public void unmark() {
        this.done = false;
    }

    public boolean getDone() {
        return this.done;
    }

    public String getName() {
        return this.name;
    }

    public String toFileString() {
        return type.getCode() + " | " + (done ? "1" : "0") + " | " + name;
    }

    @Override
    public String toString() {
        return "[" + type.getCode() + "]" + (done ? "[✓]" : "[X]") + " " + name;
    }
}