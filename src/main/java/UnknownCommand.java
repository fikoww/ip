public class UnknownCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PuyoException {
        throw new PuyoException("Sorry! I don't understand that command!");
    }
}