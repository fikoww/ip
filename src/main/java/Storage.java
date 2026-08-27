import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return tasks;

        try (Scanner sc = new Scanner(file)) {
            int lineNum = 0;
            while (sc.hasNextLine()) {
                lineNum++;
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                try {
                    String[] parts = line.split(" \\| ");
                    if (parts.length < 3) { System.out.println(" [Warning] Skipping corrupted line " + lineNum + ": " + line); continue; }
                    String type = parts[0].trim();
                    boolean done = parts[1].trim().equals("1");
                    String name = parts[2].trim();
                    Task task;
                    switch (type) {
                        case "T": task = new ToDo(name); break;
                        case "D":
                            if (parts.length < 4) { System.out.println(" [Warning] Skipping corrupted deadline at line " + lineNum); continue; }
                            LocalDateTime by = Parser.parseDateTime(parts[3].trim());
                            if (by == null) { System.out.println(" [Warning] Skipping corrupted deadline date at line " + lineNum); continue; }
                            task = new Deadline(name, by); break;
                        case "E":
                            if (parts.length < 5) { System.out.println(" [Warning] Skipping corrupted event at line " + lineNum); continue; }
                            LocalDateTime start = Parser.parseDateTime(parts[3].trim());
                            LocalDateTime end = Parser.parseDateTime(parts[4].trim());
                            if (start == null || end == null) { System.out.println(" [Warning] Skipping corrupted event date at line " + lineNum); continue; }
                            task = new Event(name, start, end); break;
                        default: System.out.println(" [Warning] Skipping unknown task type at line " + lineNum); continue;
                    }
                    if (done) task.markDone();
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