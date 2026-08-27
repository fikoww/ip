package puyo.command;

import puyo.PuyoException;
import puyo.storage.Storage;
import puyo.task.TaskList;
import puyo.ui.Ui;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PuyoException {
        ui.showTaskList(tasks);
    }
}