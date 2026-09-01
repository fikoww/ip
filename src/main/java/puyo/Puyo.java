package puyo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import puyo.command.Command;
import puyo.parser.Parser;
import puyo.storage.Storage;
import puyo.task.TaskList;
import puyo.ui.Ui;

public class Puyo {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Puyo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            tasks = new TaskList();
        }
    }

    public Puyo() {
        this("data/puyo.txt");
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.setOut(originalOut);
        }

        return outContent.toString().trim();
    }

    public static void main(String[] args) {
        new Puyo("data/puyo.txt").run();
    }
}
