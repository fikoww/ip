package puyo.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import puyo.storage.Storage;
import puyo.task.Task;
import puyo.task.TaskList;
import puyo.ui.Ui;

/**
 * Represents a command to view schedules or tasks for a specific date.
 */
public class ViewScheduleCommand extends Command {

    private final LocalDate date;

    /**
     * Constructs a {@code ViewScheduleCommand} with the specified date.
     *
     * @param date The date to filter tasks by.
     */
    public ViewScheduleCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        // Format LocalDate to match common task date styles if needed, e.g., "Sept 03 2026"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
        String formattedDate = date.format(formatter); // e.g., "Sep 03 2026" or "Sept 03 2026"

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String taskString = task.toString();

            // Extract components or handle custom month abbreviations like 'Sept'
            String targetYear = String.valueOf(date.getYear());
            String targetDay = String.format("%02d", date.getDayOfMonth()); // e.g. "03"
            String targetMonthName = date.getMonth()
                                         .getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);

            // Check if task contains the year, day, and matches the month name (handling 'Sept' vs 'Sep')
            boolean matchesYear = taskString.contains(targetYear);
            boolean matchesDay = taskString.contains(targetDay);
            boolean matchesMonth = taskString.contains(targetMonthName) || taskString.contains("Sept");

            if (matchesYear && matchesDay && matchesMonth) {
                matchingTasks.add(task);
            }
        }

        if (matchingTasks.isEmpty()) {
            ui.showMessage("No schedules found for date: " + date);
        } else {
            StringBuilder sb = new StringBuilder("Here are the schedules for " + date + ":\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                sb.append((i + 1)).append(". ").append(matchingTasks.get(i)).append("\n");
            }
            ui.showMessage(sb.toString().trim());
        }
    }
}
