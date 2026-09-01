package puyo;

import java.io.File;

import puyo.command.Command;
import puyo.parser.Parser;
import puyo.storage.Storage;
import puyo.task.TaskList;
import puyo.ui.Ui;

/**
 * Represents the main entry point and driver for the Puyo task management application.
 */
public class PuyoTest {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a {@code Puyo} application instance with the specified file path for data storage.
     *
     * @param filePath The file path where task data is saved and loaded.
     */
    public PuyoTest(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Processes a single user input command and returns the execution result or error message.
     *
     * @param input The command string entered by the user.
     * @return A status message indicating success or an error message if execution fails.
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
     * Returns the current task list managed by the application.
     *
     * @return The {@code TaskList} object.
     */
    public TaskList getTasks() {
        return this.tasks;
    }

    /**
     * Starts the main command loop of the application, reading user inputs until an exit command is given.
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
     * Launches the Puyo application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Puyo("data" + File.separator + "puyo.txt").run();
    }
}
