package io.dlrwee.todocli;

import io.dlrwee.todocli.model.Task;
import io.dlrwee.todocli.service.TaskService;
import io.dlrwee.todocli.util.ConsoleReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class App {
    private final Scanner scanner;
    private final TaskService service;
    private final List<MenuOption> options;
    private boolean running;

    public App(TaskService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
        this.options = new ArrayList<>();
        this.running = true;
        initOptions();
    }

    private void initOptions() {
        options.add(new MenuOption("Add task", this::handleCreateTask));
        options.add(new MenuOption("Load preview samples", this::handleAddPreviewSamples));
        options.add(new MenuOption("Print all (by date)", this::handlePrintAllByDate));
        options.add(new MenuOption("Print all (by title)", this::handlePrintAllByTitle));
        options.add(new MenuOption("Print all (by description)", this::handlePrintAllByDescription));
        options.add(new MenuOption("Toggle task completed status", this::handleToggleTaskStatus));
        options.add(new MenuOption("Delete task", this::handleDeleteTask));
        options.add(new MenuOption("Delete all", this::handleDeleteAll));
        options.add(new MenuOption("Exit", this::exit));
    }

    public void run() {
        System.out.println("\n===================================");
        System.out.println("      🚀 TODO CLI APPLICATION    ");
        System.out.println("===================================");

        while (running) {
            System.out.println("\n┌── NAVIGATION MENU ──────────────────────────────────┐");
            printlnOptions();
            System.out.println("└─────────────────────────────────────────────────────┘");
            System.out.print("\n🎯 Choose an option > ");

            int choice = getChoice();
            try {
                handleChoice(choice);
            } catch (RuntimeException e) {
                System.out.printf("\n💥 FATAL ERROR: %s%n", e.getMessage());
                exit();
            }
        }
    }

    private void printlnOptions() {
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("│  %d. %-47s │%n", i + 1, options.get(i).description());
        }
    }

    private int getChoice() {
        return ConsoleReader.readValidInt(
                choice -> choice >= 1 && choice <= options.size(),
                "❌ Invalid menu option. Please try again.",
                scanner
        );
    }

    private void handleChoice(int choice) {
        options.get(choice - 1).execute();
    }

    private void handleCreateTask() {
        System.out.println("\n📝 --- Create New Task ---");

        System.out.print("📌 Enter title: ");
        String title = ConsoleReader.readValidString(
                t -> !t.isBlank(),
                "⚠️ Title cannot be empty!",
                scanner
        );

        System.out.print("💬 Enter description: ");
        String description = ConsoleReader.readValidString(
                t -> !t.isBlank(),
                "⚠️ Description cannot be empty!",
                scanner
        );

        service.addTask(title, description, LocalDateTime.now(), false);
        System.out.println("\n✨ Task successfully created!");
    }

    private void handlePrintAllByTitle() {
        printAllBy(Comparator.comparing(Task::getTitle));
    }

    private void handlePrintAllByDescription() {
        printAllBy(Comparator.comparing(Task::getDescription));
    }

    private void handlePrintAllByDate() {
        printAllBy(Comparator.comparing(Task::getCreatedAt).reversed());
    }

    private void printAllBy(Comparator<Task> comparator) {
        List<Task> tasks = service.getAllTasksSorted(comparator);
        if (tasks.isEmpty()) {
            System.out.println("\n📭 Task list is empty.");
            return;
        }
        printTasks(tasks);
    }

    private void handleToggleTaskStatus() {
        getSelectedTask().ifPresent(task -> {
            service.toggleTaskStatus(task.getId());
            System.out.println("\n🔄 Task status successfully updated!");
        });
    }

    private void handleDeleteTask() {
        getSelectedTask().ifPresent(task -> {
            service.deleteTask(task.getId());
            System.out.println("\n🗑️ Task successfully deleted!");
        });
    }

    private void handleDeleteAll() {
        service.deleteAll();
        System.out.println("\n🔥 All tasks have been permanently deleted!");
    }

    private void handleAddPreviewSamples() {
        var titlesStreamUrl = App.class.getResourceAsStream("/titles.csv");
        var descriptionsStreamUrl = App.class.getResourceAsStream("/descriptions.csv");

        if (titlesStreamUrl == null || descriptionsStreamUrl == null) {
            System.out.println("\n🛑 Failed to load samples: Resource files not found.");
            return;
        }

        try (BufferedReader titleReader = new BufferedReader(
                new InputStreamReader(titlesStreamUrl, StandardCharsets.UTF_8));
             BufferedReader descReader = new BufferedReader(
                     new InputStreamReader(descriptionsStreamUrl, StandardCharsets.UTF_8))) {

            List<String> titles = titleReader.lines().toList();
            List<String> descriptions = descReader.lines().toList();

            int taskCount = Math.min(titles.size(), descriptions.size());
            LocalDateTime now = LocalDateTime.now();

            var random = ThreadLocalRandom.current();

            for (int i = 0; i < taskCount; i++) {
                LocalDateTime createdAt = now.minusDays(random.nextLong(1, 20));
                boolean completed = random.nextBoolean();

                service.addTask(titles.get(i), descriptions.get(i), createdAt, completed);
            }
            System.out.println("\n📦 Preview samples loaded successfully.");

        } catch (IOException e) {
            System.out.printf("\n🛑 Failed to load samples: %s%n", e.getMessage());
        }
    }

    private void exit() {
        running = false;
        System.out.println("\n👋 Goodbye! Have a great day!");
    }

    private Optional<Task> getSelectedTask() {
        List<Task> tasks = service.getAllTasksSorted(Comparator.comparing(Task::getCreatedAt).reversed());
        if (tasks.isEmpty()) {
            System.out.println("\n📭 No tasks available to select.");
            return Optional.empty();
        }

        printTasks(tasks);
        System.out.print("\n🔢 Enter task number: ");

        int taskNumber = ConsoleReader.readValidInt(
                number -> number >= 1 && number <= tasks.size(),
                "⚠️ Invalid task number. Please select from the list.",
                scanner
        );
        return Optional.of(tasks.get(taskNumber - 1));
    }

    private void printTasks(List<Task> tasks) {
        int maxTaskLength = "Task Details".length();
        for (Task task : tasks) {
            maxTaskLength = Math.max(maxTaskLength, task.toString().length());
        }

        String contentHeader = "Task Details";
        String borderLine = "─".repeat(maxTaskLength + 2);
        String formatString = "│ %-4d │ %-" + maxTaskLength + "s │%n";
        String headerFormat = "│ #    │ %-" + maxTaskLength + "s │%n";

        System.out.println("\n📋 --- TASK LIST ---");
        System.out.printf("┌──────┬%s┐%n", borderLine);
        System.out.printf(headerFormat, contentHeader);
        System.out.printf("├──────┼%s┤%n", borderLine);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf(formatString, (i + 1), tasks.get(i));
        }
        System.out.printf("└──────┴%s┘%n", borderLine);
    }

    private record MenuOption(String description, Runnable action) {
        public void execute() {
            action.run();
        }
    }
}
