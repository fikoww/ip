package puyo;

import puyo.command.Command;
import puyo.parser.Parser;
import puyo.storage.Storage;
import puyo.task.TaskList;
import puyo.ui.Ui;
import java.io.File;

public class Puyo {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    public Puyo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
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
                isExit = c.isExit();  // ← harus di dalam try, setelah execute
            } catch (PuyoException e) {
                ui.showError(e.getMessage());
            }  finally {
                ui.showLine();
            }
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Puyo("data" + File.separator + "puyo.txt").run();
    }
}