package puyo.task;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a collection of tasks and provides operations to manipulate the list.
 */
public class TaskList {

    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty {@code TaskList}.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a {@code TaskList} initialized with an existing list of tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Initial task list should not be null";
        this.tasks = tasks;
    }

    /**
     * Constructs a {@code TaskList} initialized with one or more tasks using varargs.
     *
     * @param tasks Tasks to initialize the list with.
     */
    public TaskList(Task... tasks) {
        assert tasks != null : "Task varargs array should not be null";
        this.tasks = new ArrayList<>();
        Collections.addAll(this.tasks, tasks);
    }

    /**
     * Adds multiple tasks to the task list using varargs.
     *
     * @param tasks Tasks to be added to the list.
     */
    public void addTasks(Task... tasks) {
        assert tasks != null : "Tasks to add should not be null";
        Collections.addAll(this.tasks, tasks);
    }

    /**
     * Adds a single task to the task list.
     *
     * @param task Task to be added.
     */
    public void add(Task task) {
        assert task != null : "Task to add should not be null";
        this.tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The zero-based index of the task to be removed.
     * @return The removed {@code Task}.
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "Index to remove out of bounds: " + index;
        return tasks.remove(index);
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The {@code Task} at the specified index.
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Index to get out of bounds: " + index;
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Total count of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the task list is empty.
     *
     * @return {@code true} if the list contains no tasks, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return An {@code ArrayList} containing all tasks.
     */
    public ArrayList<Task> getTasks() {
        assert tasks != null : "Internal tasks list should never be null";
        return tasks;
    }

    /**
     * Searches for tasks containing the specified keyword in their description.
     *
     * @param keyword The search term.
     * @return A new {@code TaskList} containing matching tasks.
     */
    public TaskList findTasks(String keyword) {
        assert keyword != null : "Search keyword should not be null";
        TaskList matching = new TaskList();
        for (Task task : tasks) {
            assert task != null : "Tasks in list should not be null";
            if (task.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matching.add(task);
            }
        }
        return matching;
    }
}
