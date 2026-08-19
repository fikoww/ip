import java.util.Scanner;

public class Puyo {
    public static void main(String[] args) {
        String banner = "██████╗ ██╗   ██╗██╗   ██╗ ██████╗\n" +
                        "██╔══██╗██║   ██║╚██╗ ██╔╝██╔═══██╗\n" +
                        "██████╔╝██║   ██║ ╚████╔╝ ██║   ██║\n" +
                        "██╔═══╝ ██║   ██║  ╚██╔╝  ██║   ██║\n" +
                        "██║     ╚██████╔╝   ██║   ╚██████╔╝\n" +
                        "╚═╝      ╚═════╝    ╚═╝    ╚═════╝";
        System.out.println("━".repeat(70));
        System.out.println(banner);
        System.out.println("━".repeat(70));
        System.out.println("Hello! My name is Puyo!");
        System.out.println("I'm a penguin!");
        System.out.println("Regardless, I can do a lot of things to make your life easier!");
        System.out.println("━".repeat(70));

        Scanner scanner = new Scanner(System.in);
        System.out.println("  Type something! (type 'bye' to exit)");
        System.out.println("━".repeat(70));

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            System.out.println("─".repeat(70));

            if (input.equals("bye")) {
                System.out.println("Okay. See you again! Bye!");
                break;
            }

            System.out.println(input);
            System.out.println("─".repeat(70));
        }

        scanner.close();
    }
}
