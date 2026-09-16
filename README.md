# Puyo

Puyo is a penguin-themed desktop task manager built with Java and JavaFX. Type commands in its chat interface to keep track of things to do, deadlines, and events.

This repository contains my CS2103T individual project (iP).

## Features

- Add todos, deadlines, and events.
- View all tasks and mark them as completed or incomplete.
- Search task descriptions by keyword.
- Delete tasks you no longer need.
- Query schedules for a date with `viewschedule`.
- Save tasks locally and load them when the application starts.

## Getting started

Puyo requires **Java 25**. To build the application from source, install a **JDK 25** and download or clone this repository.

Open a terminal in the project folder and check the Java version:

```bash
java -version
```

Start the graphical application using the included Gradle wrapper:

```bash
./gradlew run
```

On Windows, use `gradlew.bat run` instead. A separate Gradle installation is not required. The first build needs internet access to download Gradle and project dependencies.

In IntelliJ IDEA, open the project folder, import the Gradle project, and set both the Project SDK and Gradle JVM to JDK 25. The GUI entry point is `puyo.Launcher`.

## Commands

Enter a command in the text field and press Enter or click the send button.

| Command | Example | Purpose |
| --- | --- | --- |
| `todo DESCRIPTION` | `todo read chapter 6` | Add a task without a date. |
| `deadline DESCRIPTION /by DATE_TIME` | `deadline submit report /by 2026-09-18 2359` | Add a task with a deadline. |
| `event DESCRIPTION /from DATE_TIME /to DATE_TIME` | `event study group /from 2026-09-17 1400 /to 2026-09-17 1600` | Add an event with a start and end time. |
| `list` | `list` | Show all tasks. |
| `mark NUMBER` | `mark 1` | Mark a task as completed. |
| `unmark NUMBER` | `unmark 1` | Mark a task as incomplete. |
| `delete NUMBER` | `delete 1` | Remove a task. |
| `find KEYWORD` | `find report` | Find descriptions containing a keyword, ignoring letter case. |
| `viewschedule DATE` | `viewschedule 2026-09-18` | Request the schedule for a date. |
| `bye` | `bye` | Save tasks and exit. |

Use `yyyy-MM-dd` for dates and `yyyy-MM-dd HHmm` for dates with a 24-hour time. For example, `2026-09-18 1800` means 18 September 2026 at 6 pm. A date without a time is interpreted as midnight. An event's start must be earlier than its end.

Task numbers start at **1**. Use the numbers from the full `list` output when marking, unmarking, or deleting tasks; search and schedule results are numbered separately. `[X]` means incomplete and `[✓]` means completed.

The current `viewschedule` implementation matches the formatted task text. Its results can include false matches and may omit dates between the start and end of a multi-day event. Use `list` to check the full task details.

## Data storage

Puyo stores tasks in `data/puyo.txt`, relative to the directory from which the application is launched. If the file does not exist, Puyo starts with an empty task list and attempts to create the folder and file when saving.

Task additions, deletions, and changes to completion status are saved automatically. Launch Puyo from the same directory each time to use the same saved task list.

## Building a runnable JAR

With Java 25 selected, run:

```bash
./gradlew clean shadowJar
```

This produces `build/libs/puyo.jar`, which includes the application's dependencies. On Windows, use `gradlew.bat clean shadowJar`.

Copy `puyo.jar` to a separate folder, open a terminal there, and launch it with:

```bash
java -jar puyo.jar
```

Before distributing the JAR, test it from an empty folder and on the operating systems you intend to support.

## Tests and code style

Run the automated tests:

```bash
./gradlew test
```

Run the tests and configured Checkstyle checks:

```bash
./gradlew check
```

On Windows, replace `./gradlew` with `gradlew.bat`.

## Project structure

| Location | Contents |
| --- | --- |
| `src/main/java/puyo/` | Application coordination, GUI launcher, window controller, and chat components. |
| `src/main/java/parser/` | Command and date parsing. |
| `src/main/java/command/` | Implementations of user commands. |
| `src/main/java/task/` | Task models and task-list operations. |
| `src/main/java/storage/` | Loading and saving task data. |
| `src/main/java/ui/` | Text input and response formatting. |
| `src/main/resources/` | FXML layouts, CSS, and avatar images. |
| `src/test/java/` | Automated tests. |
| `docs/` | Product documentation. |

## Acknowledgements

### Libraries

- [JavaFX / OpenJFX](https://openjfx.io/) is used to build Puyo's graphical user interface.
- [JUnit](https://junit.org/) is used for automated testing.

### Development tools

- [Shadow Gradle Plugin](https://gradleup.com/shadow/) is used to package Puyo and its dependencies into a runnable JAR.
- [Checkstyle](https://checkstyle.org/) is used to check compliance with coding standards.

### AI assistance

ChatGPT was used by [fikoww](https://github.com/fikoww) to help explain the Puyo codebase, interpret project requirements, and draft this README, including its acknowledgements.

<!-- TODO before final submission: Extend the disclosure above if AI was also used for code, tests, GUI changes, other documentation, or images. State the actual tool, who used it, and the extent of use. Cite localized code assistance near the relevant methods/classes, as required by the course. Do not claim work or review that did not take place. -->

### Images

- `src/main/resources/images/puyo.png`: https://storage.googleapis.com/dskaigdjhfmhqe/apparel-with-penguin-logo.html
- `src/main/resources/images/user.png`: https://www.magnific.com/premium-vector/black-white-boy-illustration-doodle-artwork_176795745.htm

<!-- TODO before final submission: Credit reused GUI images near the first screenshot showing them in docs/README.md as well. Course-provided assets are exempt under the supplied course policy. -->

<!-- TODO before final submission: If external code or documentation was reused, add the original source at the relevant location. For code inspired by a source or substantially adapted from it, use an appropriate source comment. Enclose non-trivial copied code with only minor changes in //@@author fikoww-reused and //@@author tags, with a source citation. Reuse from course materials does not require credit under the course policy. -->
