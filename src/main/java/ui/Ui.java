package puyo.ui;

import puyo.task.Task;
import puyo.task.TaskList;
import java.util.Scanner;

public class Ui {

    private static final String LINE = "─".repeat(70);
    private static final String SOLID_LINE = "━".repeat(70);
    private static final String BANNER =
                    "██████╗ ██╗   ██╗██╗   ██╗ ██████╗\n" +
                    "██╔══██╗██║   ██║╚██╗ ██╔╝██╔═══██╗\n" +
                    "██████╔╝██║   ██║ ╚████╔╝ ██║   ██║\n" +
                    "██╔═══╝ ██║   ██║  ╚██╔╝  ██║   ██║\n" +
                    "██║     ╚██████╔╝   ██║   ╚██████╔╝\n" +
                    "╚═╝      ╚═════╝    ╚═╝    ╚═════╝";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(SOLID_LINE);
        System.out.println(BANNER);
        System.out.println(SOLID_LINE);
        System.out.println("Hello! My name is Puyo!");
        System.out.println("I'm a penguin!");
        System.out.println("Regardless, I can do a lot of things to make your life easier!");
        System.out.println(SOLID_LINE);
        System.out.println("  Type something! (type 'bye' to exit)");
        System.out.println(SOLID_LINE);
    }

    public void showBye() {
        System.out.println("Okay. See you again! Bye!");
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println(" Error: " + message);
    }

    public void showTaskList(TaskList tasks) {
        System.out.println("(Note: X means you haven't done the task yet, ✓ means you have done the task)");
        if (tasks.isEmpty()) {
            System.out.println(" (no tasks yet)");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + tasks.get(i));
            }
        }
    }

    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" Don't forget to do this!");
        System.out.println(" " + task);
        System.out.println(" (You now have " + totalTasks + " tasks to do! Good luck!)");
    }

    public String readCommand() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }
}