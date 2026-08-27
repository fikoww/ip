package puyo.command;

import puyo.PuyoException;
import puyo.storage.Storage;
import puyo.task.TaskList;
import puyo.ui.Ui;

public abstract class Command {

    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws PuyoException;

    public boolean isExit() {
        return false;
    }
}