package puyo.task;

public abstract class Task {

    String name;
    boolean done;
    TaskType type;

    Task(String name, TaskType type) {
        this.name = name.trim();
        this.done = false;
        this.type = type;
    }

    void markDone() {
        this.done = true;
    }

    void unmark() {
        this.done = false;
    }

    public String toFileString() {
        return type.getCode() + " | " + (done ? "1" : "0") + " | " + name;
    }

    @Override
    public String toString() {
        return "[" + type.getCode() + "]" + (done ? "[✓]" : "[X]") + " " + name;
    }
}