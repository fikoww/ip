package puyo.command;

import puyo.PuyoException;
import puyo.storage.Storage;
import puyo.task.Task;
import puyo.task.TaskList;
import puyo.ui.Ui;

public class UnmarkCommand extends Command {

    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PuyoException {
        if (index < 0 || index >= tasks.size()) {
            throw new PuyoException("Invalid task number.");
        }
        Task task = tasks.get(index);
        if (!task.done) {
            ui.showMessage(" You indeed haven't done this task!");
        } else {
            task.unmark();
            ui.showMessage(" Don't forget to " + task.name + "!");
            ui.showMessage(" " + task);
            storage.save(tasks);
        }
    }
}