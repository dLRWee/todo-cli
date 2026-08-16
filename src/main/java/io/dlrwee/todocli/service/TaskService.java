package io.dlrwee.todocli.service;

import io.dlrwee.todocli.exception.impl.NoSuchTaskTodoException;
import io.dlrwee.todocli.model.Task;
import io.dlrwee.todocli.repository.TaskRepository;
import io.dlrwee.todocli.util.TaskValidator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void addTask(String title, String description, LocalDateTime createdAt, boolean completed) {
        TaskValidator.validateTitle(title);
        TaskValidator.validateDescription(description);

        Task task = Task.builder(title, description)
                .setCreatedAt(createdAt)
                .setCompleted(completed)
                .build();

        repository.save(task);
    }

    public List<Task> getAllTasksSorted(Comparator<Task> comparator) {
        return repository.findAll().stream()
                .sorted(comparator)
                .toList();
    }

    public void toggleTaskStatus(UUID id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoSuchTaskTodoException(id));
        task.setCompleted(!task.isCompleted());

        repository.save(task);
    }

    public void deleteTask(UUID id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.clear();
    }
}
