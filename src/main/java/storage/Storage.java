package puyo.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import puyo.parser.Parser;
import puyo.task.Deadline;
import puyo.task.Event;
import puyo.task.Task;
import puyo.task.TaskList;
import puyo.task.ToDo;

/**
 * Handles loading tasks from and saving tasks to a local storage file.
 */
public class Storage {

    private final String filePath;

    /**
     * Constructs a {@code Storage} instance with the specified target file path.
     *
     * @param filePath The file path where tasks are stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path configured for storage.
     *
     * @return The target storage file path string.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Loads saved tasks from the local storage file.
     *
     * @return An {@code ArrayList} of parsed {@code Task} objects, or an empty list
     *         if file doesn't exist.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }

        try (Scanner sc = new Scanner(file)) {
            int lineNum = 0;
            while (sc.hasNextLine()) {
                lineNum++;
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Task task = parseTaskFromLine(line);
                    if (task != null) {
                        tasks.add(task);
                    } else {
                        System.out.println(" [Warning] Skipping corrupted line " + lineNum + ": " + line);
                    }
                } catch (Exception e) {
                    System.out.println(" [Warning] Skipping corrupted line " + lineNum + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println(" [Warning] Could not load tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Parses a single formatted line from the storage file into a {@code Task}.
     *
     * @param line Raw line read from file.
     * @return The parsed {@code Task}, or {@code null} if formatted incorrectly.
     */
    private Task parseTaskFromLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean done = parts[1].trim().equals("1");
        String name = parts[2].trim();

        Task task = createSpecificTask(type, parts, name);
        if (task == null) {
            return null;
        }

        if (done) {
            task.markDone();
        }
        return task;
    }

    /**
     * Factory method to instantiate specific Task subclasses depending on type code.
     */
    private Task createSpecificTask(String type, String[] parts, String name) {
        switch (type) {
            case "T":
                return new ToDo(name);
            case "D":
                if (parts.length < 4) {
                    return null;
                }
                LocalDateTime by = Parser.parseDateTime(parts[3].trim());
                return (by != null) ? new Deadline(name, by) : null;
            case "E":
                if (parts.length < 5) {
                    return null;
                }
                LocalDateTime start = Parser.parseDateTime(parts[3].trim());
                LocalDateTime end = Parser.parseDateTime(parts[4].trim());
                return (start != null && end != null) ? new Event(name, start, end) : null;
            default:
                return null;
        }
    }

    /**
     * Saves the current list of tasks to the local storage file.
     *
     * @param tasks The {@code TaskList} containing tasks to save.
     */
    public void save(TaskList tasks) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            for (int i = 0; i < tasks.size(); i++) {
                fw.write(tasks.get(i).toFileString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(" [Warning] Could not save tasks: " + e.getMessage());
        }
    }
}
