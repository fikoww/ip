import java.util.Scanner;
import java.util.ArrayList;

public class Puyo {

    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN
    }

    public enum TaskType {
        TODO("[T]"),
        DEADLINE("[D]"),
        EVENT("[E]");

        private final String code;

        TaskType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

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

        @Override
        public String toString() {
            return type.getCode() + (done ? "[✓]" : "[X]") + " " + name;
        }
    }

    public static class ToDo extends Task {
        public ToDo(String description) {
            super(description, TaskType.TODO);
        }
    }

    public static class Deadline extends Task {
        String by;

        public Deadline(String description, String by) {
            super(description, TaskType.DEADLINE);
            this.by = by;
        }

        @Override
        public String toString() {
            return super.toString() + " (by: " + by + ")";
        }
    }

    public static class Event extends Task {
        String start;
        String end;

        public Event(String description, String start, String end) {
            super(description, TaskType.EVENT);
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return super.toString() + " (from: " + start + " to: " + end + ")";
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

    public static void main(String[] args) {
        String line = "─".repeat(70);
        String solidline = "━".repeat(70);

        ArrayList<Task> tasks = new ArrayList<>();

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
                        int index = Integer.parseInt(input.substring(4).trim()) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task task = tasks.get(index);
                            if (task.done) {
                                System.out.println(" You indeed have done this task!");
                            } else {
                                task.markDone();
                                System.out.println(" Amazing! Don't forget to take a rest!");
                                System.out.println(" " + task);
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
                        int index = Integer.parseInt(input.substring(6).trim()) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task task = tasks.get(index);
                            if (!task.done) {
                                System.out.println(" You indeed haven't done this task!");
                            } else {
                                task.unmark();
                                System.out.println(" Don't forget to " + task.name + "!");
                                System.out.println(" " + task);
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
                        int index = Integer.parseInt(input.substring(6).trim()) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task removedTask = tasks.remove(index);
                            System.out.println(" Noted. I've removed this task:");
                            System.out.println("   " + removedTask);
                            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
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
                        Task newTask = new ToDo(todoDesc);
                        tasks.add(newTask);
                        System.out.println(" Don't forget to do this!");
                        System.out.println(" " + newTask);
                        System.out.println(" (You now have " + tasks.size() + " tasks to do! Good luck!)");
                    }
                    break;

                case DEADLINE:
                    String lowerInputDeadline = input.toLowerCase();
                    if (!lowerInputDeadline.contains("/by")) {
                        System.out.println("Please enter a valid deadline by using '/by'!");
                        break;
                    }
                    String[] deadlineParts = input.substring(8).split("/by", 2);
                    String deadlineName = deadlineParts[0].trim();
                    String by = (deadlineParts.length > 1) ? deadlineParts[1].trim() : "";

                    if (deadlineName.isEmpty() || by.isEmpty()) {
                        System.out.println("The description or time of a deadline can't be empty!");
                    } else {
                        Task newTask = new Deadline(deadlineName, by);
                        tasks.add(newTask);
                        System.out.println(" Don't forget to do this!");
                        System.out.println(" " + newTask);
                        System.out.println(" (You now have " + tasks.size() + " tasks to do! Good luck!)");
                    }
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

                    String[] eventParts = input.substring(5).split("/from|/to");
                    if (eventParts.length < 3) {
                        System.out.println("Event description, '/from', or '/to' cannot be empty!");
                        break;
                    }

                    String eventName = eventParts[0].trim();
                    String from = eventParts[1].trim();
                    String to = eventParts[2].trim();

                    if (eventName.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        System.out.println("Event description, '/from', or '/to' cannot be empty!");
                    } else {
                        Task newTask = new Event(eventName, from, to);
                        tasks.add(newTask);
                        System.out.println(" Don't forget to do this!");
                        System.out.println(" " + newTask);
                        System.out.println(" (You now have " + tasks.size() + " tasks to do! Good luck!)");
                    }
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