public class DeleteCommand extends Command {

    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PuyoException {
        if (index < 0 || index >= tasks.size()) {
            throw new PuyoException("Invalid task number.");
        }
        Task removed = tasks.remove(index);
        ui.showMessage(" Noted. I've removed this task:");
        ui.showMessage("   " + removed);
        ui.showMessage(" Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks);
    }
}