import java.util.Scanner;
import java.util.ArrayList;

public class Puyo {
    static class Task {
        String name;
        boolean done;

        Task(String name) {
            this.name = name.trim();
            this.done = false;
        }

        void markDone() {
            this.done = true;
        }

        void unmark() {
            this.done = false;
        }

        public String toString() {
            return (done ? "[✓]" : "[X]") + " " + name;
        }
    }

    public static class Deadline extends Task {
        String by;

        public Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        public String toString() {
            return "[D]" + super.toString() + " (by: " + by + ")";
        }
    }

    public static class ToDo extends Task {
        public ToDo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }
    }

    public static class Event extends Task {
        String start;
        String end;

        public Event(String description, String start, String end) {
            super(description);
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
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

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Okay. See you again! Bye!");
                System.out.println(line);
                break;
            }

            else if (input.equalsIgnoreCase("list")) {
                System.out.println("(Note: X means you haven't done the task yet, ✓ means you have done the task)");
                if (tasks.isEmpty()) {
                    System.out.println(" (no tasks yet)");
                } else {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks.get(i));
                    }
                }
                System.out.println(line);
            }

            else if (input.toLowerCase().startsWith("mark")) {
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
                System.out.println(line);
            }

            else if (input.toLowerCase().startsWith("unmark")) {
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
                System.out.println(line);
            }

            else if (input.toLowerCase().startsWith("delete")) {
                try {
                    int index = Integer.parseInt(input.substring(6).trim()) - 1;
                    if (index >= 0 && index < tasks.size()) {
                        Task removedTask = tasks.remove(index);
                        System.out.println(" Noted. I have removed the task:");
                        System.out.println("   " + removedTask);
                        System.out.println(" Now you have " + tasks.size() + " remaining tasks in the list.");
                    }
                    else {
                        System.out.println(" Invalid task number.");
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    System.out.println(" Please provide a valid task number to delete! (e.g. delete 1)");
                }
                System.out.println(line);
            }

            else {
                if (input.isBlank()) {
                    System.out.println("Please enter a non-empty valid command!");
                    System.out.println(line);
                    continue;
                }

                String lowerInput = input.toLowerCase();
                Task newTask = null;

                // 1. TODO
                if (lowerInput.startsWith("todo")) {
                    String desc = input.substring(4).trim();
                    if (desc.isEmpty()) {
                        System.out.println("The description of a todo can't be empty!");
                        System.out.println(line);
                        continue;
                    }
                    newTask = new ToDo(desc);
                }

                // 2. DEADLINE
                else if (lowerInput.startsWith("deadline")) {
                    if (!lowerInput.contains("/by")) {
                        System.out.println("Please enter a valid deadline by using '/by'!");
                        System.out.println(line);
                        continue;
                    }
                    String[] parts = input.substring(8).split("/by", 2);
                    String name = parts[0].trim();
                    String by = (parts.length > 1) ? parts[1].trim() : "";

                    if (name.isEmpty() || by.isEmpty()) {
                        System.out.println("The description or time of a deadline can't be empty!");
                        System.out.println(line);
                        continue;
                    }
                    newTask = new Deadline(name, by);
                }

                // 3. EVENT
                else if (lowerInput.startsWith("event")) {
                    boolean hasFrom = lowerInput.contains("/from");
                    boolean hasTo = lowerInput.contains("/to");

                    if (!hasFrom && !hasTo) {
                        System.out.println("Please enter a valid event timing by using '/from' and '/to'!");
                        System.out.println(line);
                        continue;
                    } else if (!hasFrom) {
                        System.out.println("Please enter a valid starting event timing by using '/from'!");
                        System.out.println(line);
                        continue;
                    } else if (!hasTo) {
                        System.out.println("Please enter a valid ending event timing by using '/to'!");
                        System.out.println(line);
                        continue;
                    } else if (lowerInput.indexOf("/from") > lowerInput.indexOf("/to")) {
                        System.out.println("Please enter a valid event timing by putting '/from' before '/to'!");
                        System.out.println(line);
                        continue;
                    }

                    String[] parts = input.substring(5).split("/from|/to");
                    if (parts.length < 3) {
                        System.out.println("Event description, '/from', or '/to' cannot be empty!");
                        System.out.println(line);
                        continue;
                    }

                    String name = parts[0].trim();
                    String from = parts[1].trim();
                    String to = parts[2].trim();

                    if (name.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        System.out.println("Event description, '/from', or '/to' cannot be empty!");
                        System.out.println(line);
                        continue;
                    }
                    newTask = new Event(name, from, to);
                }

                // 4. UNKNOWN COMMAND
                else {
                    System.out.println("Sorry! I don't understand that command!");
                    System.out.println(line);
                    continue;
                }

                tasks.add(newTask);
                System.out.println(" Don't forget to do this!");
                System.out.println(" " + newTask);
                System.out.println(" (You now have " + tasks.size() + " tasks to do! Good luck!)");
                System.out.println(line);
            }
        }

        scanner.close();
    }
}