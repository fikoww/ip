public class MarkCommand extends Command {

    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PuyoException {
        if (index < 0 || index >= tasks.size()) {
            throw new PuyoException("Invalid task number.");
        }
        Task task = tasks.get(index);
        if (task.done) {
            ui.showMessage(" You indeed have done this task!");
        } else {
            task.markDone();
            ui.showMessage(" Amazing! Don't forget to take a rest!");
            ui.showMessage(" " + task);
            storage.save(tasks);
        }
    }
}