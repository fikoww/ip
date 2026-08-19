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
                }
                else {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                }
                System.out.println(line);
            }

            else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                if (index >= 0 && index < taskCount) {
                    if (tasks[index].done) {
                        System.out.println(" You indeed have done this task!");
                    }
                    else {
                        tasks[index].done = true;
                        System.out.println(" Amazing! Don't forget to take a rest!");
                        System.out.println("   [✓] " + tasks[index].name);
                    }
                }
                else {
                    System.out.println(" Invalid task number.");
                }
                System.out.println(line);
            }

            else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                if (index >= 0 && index < taskCount) {
                    if (!tasks[index].done) {
                        System.out.println(" You indeed haven't done this task!");
                    }
                    else {
                        tasks[index].done = false;
                        System.out.println(" Don't forget to " + tasks[index].name + "!");
                        System.out.println(" " + tasks[index]);
                    }
                }
                else {
                    System.out.println(" Invalid task number.");
                }
                System.out.println(line);
            }

            else {
                if (taskCount == 100) {
                    System.out.println("Too many tasks! Please complete one task first!");
                    System.out.println(line);
                }
                else {
                    tasks[taskCount] =  new Task(input);
                    System.out.println(" Added: " + input);
                    taskCount++;
                    System.out.println(" (tasks: " + taskCount + ")");
                    System.out.println(line);
                }
            }
        }

        scanner.close();
    }
}
