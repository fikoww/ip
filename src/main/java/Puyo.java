import java.util.Scanner;
import java.util.ArrayList;

public class Puyo {
    public static void main(String[] args) {
        String line = "─".repeat(70);
        String solidline = "━".repeat(70);
        String[] tasks = new String[100];
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
                if (taskCount == 0) {
                    System.out.println(" (no tasks yet)");
                } else {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                }
                System.out.println(line);
            }

            else {
                if (taskCount == 100) {
                    System.out.println("Too many tasks! Please complete one task first!");
                    System.out.println(line);
                }
                else {
                    tasks[taskCount] = input;
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
