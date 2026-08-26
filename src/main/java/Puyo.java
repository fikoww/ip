import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Puyo {

    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN
    }

    public enum TaskType {
        TODO("T"),
        DEADLINE("D"),
        EVENT("E");

        private final String code;

        TaskType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    static final DateTimeFormatter DISPLAY_DATETIME = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    static final DateTimeFormatter SAVE_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    static abstract class Task {
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

    public static class ToDo extends Task {
        public ToDo(String description) {
            super(description, TaskType.TODO);
        }
    }

    public static class Deadline extends Task {
        LocalDateTime by;

        public Deadline(String description, LocalDateTime by) {
            super(description, TaskType.DEADLINE);
            this.by = by;
        }

        @Override
        public String toFileString() {
            return super.toFileString() + " | " + by.format(SAVE_DATETIME);
        }

        @Override
        public String toString() {
            return super.toString() + " (by: " + by.format(DISPLAY_DATETIME) + ")";
        }
    }

    public static class Event extends Task {
        LocalDateTime start;
        LocalDateTime end;

        public Event(String description, LocalDateTime start, LocalDateTime end) {
            super(description, TaskType.EVENT);
            this.start = start;
            this.end = end;
        }

        @Override
        public String toFileString() {
            return super.toFileString() + " | " + start.format(SAVE_DATETIME) + " | " + end.format(SAVE_DATETIME);
        }

        @Override
        public String toString() {
            return super.toString() + " (from: " + start.format(DISPLAY_DATETIME) + " to: " + end.format(DISPLAY_DATETIME) + ")";
        }
    }

    public static LocalDateTime parseDateTime(String raw) {
        raw = raw.trim();
        try {
            return LocalDateTime.parse(raw, INPUT_DATETIME);
        } catch (DateTimeParseException e1) {
            try {
                LocalDate date = LocalDate.parse(raw, INPUT_DATE);
                return date.atStartOfDay();
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }

    public static CommandType parseCommand(String input) {
        String firstWord = input.split(" ")[0].toLowerCase();
        switch (firstWord) {
            case "bye": return CommandType.BYE;
            case "list": return CommandType.LIST;
            case "mark": return CommandType.MARK;
            case "unmark": return CommandType.UNMARK;
            case "delete": return CommandType.DELETE;
            case "todo": return CommandType.TODO;
            case "deadline": return CommandType.DEADLINE;
            case "event": return CommandType.EVENT;
            default: return CommandType.UNKNOWN;
        }
    }

    static final String FILE_PATH = "data" + File.separator + "puyo.txt";

    public static void saveTasks(ArrayList<Task> tasks) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            for (Task task : tasks) {
                fw.write(task.toFileString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(" [Warning] Could not save tasks: " + e.getMessage());
        }
    }

    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner sc = new Scanner(file)) {
            int lineNum = 0;
            while (sc.hasNextLine()) {
                lineNum++;
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

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
                                System.out.println(" [Warning] Skipping corrupted deadline at line " + lineNum + ": " + line);
                                continue;
                            }
                            LocalDateTime deadlineBy = parseDateTime(parts[3].trim());
                            if (deadlineBy == null) {
                                System.out.println(" [Warning] Skipping corrupted deadline date at line " + lineNum + ": " + line);
                                continue;
                            }
                            task = new Deadline(name, deadlineBy);
                            break;
                        case "E":
                            if (parts.length < 5) {
                                System.out.println(" [Warning] Skipping corrupted event at line " + lineNum + ": " + line);
                                continue;
                            }
                            LocalDateTime eventStart = parseDateTime(parts[3].trim());
                            LocalDateTime eventEnd = parseDateTime(parts[4].trim());
                            if (eventStart == null || eventEnd == null) {
                                System.out.println(" [Warning] Skipping corrupted event date at line " + lineNum + ": " + line);
                                continue;
                            }
                            task = new Event(name, eventStart, eventEnd);
                            break;
                        default:
                            System.out.println(" [Warning] Skipping unknown task type at line " + lineNum + ": " + line);
                            continue;
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

    public static void main(String[] args) {
        String line = "─".repeat(70);
        String solidline = "━".repeat(70);

        ArrayList<Task> tasks = loadTasks();
        boolean hasInteracted = false;

        String banner = "██████╗ ██╗   ██╗██╗   ██╗ ██████╗\n" +
                "██╔══██╗██║   ██║╚██╗ ██╔╝██╔═══██╗\n" +
                "██████╔╝██║   ██║ ╚████╔╝ ██║   ██║\n" +
                "██╔═══╝ ██║   ██║  ╚██╔╝  ██║   ██║\n" +
                "██║     ╚██████╔╝   ██║   ╚██████╔╝\n" +
                "╚═╝      ╚═════╝    ╚═╝    ╚═════╝";

        System.out.println(solidline);
        System.out.println(banner);
        System.out.println(solidline);
        System.out.println("Hello! My name is Puyo!");
        System.out.println("I'm a penguin!");
        System.out.println("Regardless, I can do a lot of things to make your life easier!");
        System.out.println(solidline);

        Scanner scanner = new Scanner(System.in);
        System.out.println("  Type something! (type 'bye' to exit)");
        System.out.println(solidline);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            System.out.println(line);

            if (input.isBlank()) {
                System.out.println("Please enter a non-empty valid command!");
                System.out.println(line);
                continue;
            }

            CommandType command = parseCommand(input);

            if (command == CommandType.BYE) {
                if (!hasInteracted) {
                    saveTasks(new ArrayList<>());
                }
                System.out.println("Okay. See you again! Bye!");
                System.out.println(line);
                break;
            }

            switch (command) {
                case LIST:
                    System.out.println("(Note: X means you haven't done the task yet, ✓ means you have done the task)");
                    if (tasks.isEmpty()) {
                        System.out.println(" (no tasks yet)");
                    } else {
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
                        }
                    }
                    break;

                case MARK:
                    try {
                        int index = Integer.parseInt(input.substring(5).trim()) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task task = tasks.get(index);
                            if (task.done) {
                                System.out.println(" You indeed have done this task!");
                            } else {
                                task.markDone();
                                System.out.println(" Amazing! Don't forget to take a rest!");
                                System.out.println(" " + task);
                                saveTasks(tasks);
                                hasInteracted = true;
                            }
                        } else {
                            System.out.println(" Invalid task number.");
                        }
                    } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                        System.out.println(" Please provide a valid task number to mark! (e.g. mark 1)");
                    }
                    break;

                case UNMARK:
                    try {
                        int index = Integer.parseInt(input.substring(7).trim()) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task task = tasks.get(index);
                            if (!task.done) {
                                System.out.println(" You indeed haven't done this task!");
                            } else {
                                task.unmark();
                                System.out.println(" Don't forget to " + task.name + "!");
                                System.out.println(" " + task);
                                saveTasks(tasks);
                                hasInteracted = true;
                            }
                        } else {
                            System.out.println(" Invalid task number.");
                        }
                    } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                        System.out.println(" Please provide a valid task number to unmark! (e.g. unmark 1)");
                    }
                    break;

                case DELETE:
                    try {
                        int index = Integer.parseInt(input.substring(7).trim()) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task removedTask = tasks.remove(index);
                            System.out.println(" Noted. I've removed this task:");
                            System.out.println("   " + removedTask);
                            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                            saveTasks(tasks);
                            hasInteracted = true;
                        } else {
                            System.out.println(" Invalid task number.");
                        }
                    } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                        System.out.println(" Please provide a valid task number to delete! (e.g. delete 1)");
                    }
                    break;

                case TODO:
                    String todoDesc = input.substring(4).trim();
                    if (todoDesc.isEmpty()) {
                        System.out.println("The description of a todo can't be empty!");
                    } else {
                        Task newTodo = new ToDo(todoDesc);
                        tasks.add(newTodo);
                        System.out.println(" Don't forget to do this!");
                        System.out.println(" " + newTodo);
                        System.out.println(" (You now have " + tasks.size() + " tasks to do! Good luck!)");
                        saveTasks(tasks);
                        hasInteracted = true;
                    }
                    break;

                case DEADLINE:
                    String lowerInputDeadline = input.toLowerCase();
                    if (!lowerInputDeadline.contains("/by")) {
                        System.out.println("Please enter a valid deadline by using '/by'!");
                        break;
                    }
                    String[] deadlineParts = input.substring(9).split("/by", 2);
                    String deadlineName = deadlineParts[0].trim();
                    String byRaw = (deadlineParts.length > 1) ? deadlineParts[1].trim() : "";
                    if (deadlineName.isEmpty() || byRaw.isEmpty()) {
                        System.out.println("The description or time of a deadline can't be empty!");
                        break;
                    }
                    LocalDateTime byDateTime = parseDateTime(byRaw);
                    if (byDateTime == null) {
                        System.out.println("Invalid date format! Use: yyyy-MM-dd or yyyy-MM-dd HHmm (e.g. 2019-12-02 1800)");
                        break;
                    }
                    Task newDeadline = new Deadline(deadlineName, byDateTime);
                    tasks.add(newDeadline);
                    System.out.println(" Don't forget to do this!");
                    System.out.println(" " + newDeadline);
                    System.out.println(" (You now have " + tasks.size() + " tasks to do! Good luck!)");
                    saveTasks(tasks);
                    hasInteracted = true;
                    break;

                case EVENT:
                    String lowerInputEvent = input.toLowerCase();
                    boolean hasFrom = lowerInputEvent.contains("/from");
                    boolean hasTo = lowerInputEvent.contains("/to");
                    if (!hasFrom && !hasTo) {
                        System.out.println("Please enter a valid event timing by using '/from' and '/to'!");
                        break;
                    } else if (!hasFrom) {
                        System.out.println("Please enter a valid starting event timing by using '/from'!");
                        break;
                    } else if (!hasTo) {
                        System.out.println("Please enter a valid ending event timing by using '/to'!");
                        break;
                    } else if (lowerInputEvent.indexOf("/from") > lowerInputEvent.indexOf("/to")) {
                        System.out.println("Please enter a valid event timing by putting '/from' before '/to'!");
                        break;
                    }
                    String[] eventParts = input.substring(6).split("/from|/to");
                    if (eventParts.length < 3) {
                        System.out.println("Event description, '/from', or '/to' cannot be empty!");
                        break;
                    }
                    String eventName = eventParts[0].trim();
                    String fromRaw = eventParts[1].trim();
                    String toRaw = eventParts[2].trim();
                    if (eventName.isEmpty() || fromRaw.isEmpty() || toRaw.isEmpty()) {
                        System.out.println("Event description, '/from', or '/to' cannot be empty!");
                        break;
                    }
                    LocalDateTime fromDateTime = parseDateTime(fromRaw);
                    LocalDateTime toDateTime = parseDateTime(toRaw);
                    if (fromDateTime == null || toDateTime == null) {
                        System.out.println("Invalid date format! Use: yyyy-MM-dd or yyyy-MM-dd HHmm (e.g. 2019-12-02 1800)");
                        break;
                    }
                    Task newEvent = new Event(eventName, fromDateTime, toDateTime);
                    tasks.add(newEvent);
                    System.out.println(" Don't forget to do this!");
                    System.out.println(" " + newEvent);
                    System.out.println(" (You now have " + tasks.size() + " tasks to do! Good luck!)");
                    saveTasks(tasks);
                    hasInteracted = true;
                    break;

                case UNKNOWN:
                default:
                    System.out.println("Sorry! I don't understand that command!");
                    break;
            }
            System.out.println(line);
        }

        scanner.close();
    }
}