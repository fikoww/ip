package puyo.command;

import puyo.PuyoException;
import puyo.storage.Storage;
import puyo.task.Task;
import puyo.task.TaskList;
import puyo.ui.Ui;

public class AddCommand extends Command {

    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PuyoException {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.save(tasks);
    }
}