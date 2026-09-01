package puyo;

import java.io.File;

import puyo.command.Command;
import puyo.parser.Parser;
import puyo.storage.Storage;
import puyo.task.TaskList;
import puyo.ui.Ui;

/**
 * Main class for the Puyo application, a personal assistant task manager.
 */
public class Puyo {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a new Puyo instance with the specified file path for data storage.
     *
     * @param filePath The path of the file where tasks are saved and loaded.
     */
    public Puyo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Processes a user input string and returns the execution response message.
     *
     * @param input The raw input command from the user.
     * @return The response string resulting from the command execution.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);
            return "Command executed successfully: " + input;
        } catch (PuyoException e) {
            return e.getMessage();
        }
    }

    /**
     * Returns the task list currently managed by Puyo.
     *
     * @return The current TaskList instance.
     */
    public TaskList getTasks() {
        return this.tasks;
    }

    /**
     * Runs the main interactive loop of the application until the exit command is issued.
     */
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
            } catch (PuyoException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.close();
    }

    /**
     * Entry point of the Puyo application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Puyo("data" + File.separator + "puyo.txt").run();
    }
}
