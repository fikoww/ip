import java.util.Scanner;

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
        Task[] tasks = new Task[100];
        int taskCount = 0;

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
                if (taskCount == 0) {
                    System.out.println(" (no tasks yet)");
                } else {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                }
                System.out.println(line);
            }

            else if (input.toLowerCase().startsWith("mark")) {
                try {
                    int index = Integer.parseInt(input.substring(4).trim()) - 1;
                    if (index >= 0 && index < taskCount) {
                        if (tasks[index].done) {
                            System.out.println(" You indeed have done this task!");
                        } else {
                            tasks[index].markDone();
                            System.out.println(" Amazing! Don't forget to take a rest!");
                            System.out.println(" " + tasks[index]);
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
                    if (index >= 0 && index < taskCount) {
                        if (!tasks[index].done) {
                            System.out.println(" You indeed haven't done this task!");
                        } else {
                            tasks[index].unmark();
                            System.out.println(" Don't forget to " + tasks[index].name + "!");
                            System.out.println(" " + tasks[index]);
                        }
                    } else {
                        System.out.println(" Invalid task number.");
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    System.out.println(" Please provide a valid task number to unmark! (e.g. unmark 1)");
                }
                System.out.println(line);
            }

            else {
                if (taskCount == 100) {
                    System.out.println("Too many tasks! Please complete one task first!");
                    System.out.println(line);
                    continue;
                }

                if (input.isBlank()) {
                    System.out.println("Please enter a non-empty valid command!");
                    System.out.println(line);
                    continue;
                }

                String lowerInput = input.toLowerCase();

                // 1. TODO COMMAND
                if (lowerInput.startsWith("todo")) {
                    String desc = input.substring(4).trim();
                    if (desc.isEmpty()) {
                        System.out.println("The description of a todo can't be empty!");
                        System.out.println(line);
                        continue;
                    }
                    tasks[taskCount] = new ToDo(desc);
                }

                // 2. DEADLINE COMMAND
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
                    tasks[taskCount] = new Deadline(name, by);
                }

                // 3. EVENT COMMAND
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
                    tasks[taskCount] = new Event(name, from, to);
                }

                // 4. UNKNOWN COMMAND
                else {
                    System.out.println("Sorry! I don't understand that command!");
                    System.out.println(line);
                    continue;
                }

                System.out.println(" Don't forget to do this!");
                System.out.println(" " + tasks[taskCount]);
                taskCount++;
                System.out.println(" (You now have " + taskCount + " tasks to do! Good luck!)");
                System.out.println(line);
            }
        }

        scanner.close();
    }
}