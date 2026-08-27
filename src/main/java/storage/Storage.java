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
     * @return An {@code ArrayList} of parsed {@code Task} objects, or an empty list if file doesn't exist.
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
                    String[] parts = line.split(" \\| ");
                    if (parts.length < 3) {
                        System.out.println(" [Warning] Skipping corrupted line " + lineNum + ": " + line);
                        continue;
                    }
                    String type = parts[0].trim();
                    boolean done = parts[1].trim().equals("1");
                    String name = parts[2].trim();
                    Task task;
                    switch (type) {
                        case "T":
                            task = new ToDo(name);
                            break;
                        case "D":
                            if (parts.length < 4) {
                                System.out.println(" [Warning] Skipping corrupted deadline at line " + lineNum);
                                continue;
                            }
                            LocalDateTime by = Parser.parseDateTime(parts[3].trim());
                            if (by == null) {
                                System.out.println(" [Warning] Skipping corrupted deadline date at line " + lineNum);
                                continue;
                            }
                            task = new Deadline(name, by);
                            break;
                        case "E":
                            if (parts.length < 5) {
                                System.out.println(" [Warning] Skipping corrupted event at line " + lineNum);
                                continue;
                            }
                            LocalDateTime start = Parser.parseDateTime(parts[3].trim());
                            LocalDateTime end = Parser.parseDateTime(parts[4].trim());
                            if (start == null || end == null) {
                                System.out.println(" [Warning] Skipping corrupted event date at line " + lineNum);
                                continue;
                            }
                            task = new Event(name, start, end);
                            break;
                        default:
                            System.out.println(" [Warning] Skipping unknown task type at line " + lineNum);
                            continue;
                    }
                    if (done) {
                        task.markDone();
                    }
                    tasks.add(task);
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