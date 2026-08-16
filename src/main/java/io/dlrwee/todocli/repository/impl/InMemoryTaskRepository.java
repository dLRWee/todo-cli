package io.dlrwee.todocli.repository.impl;

import io.dlrwee.todocli.model.Task;
import io.dlrwee.todocli.repository.TaskRepository;

import java.util.*;

public final class InMemoryTaskRepository implements TaskRepository {

    private final Map<UUID, Task> storage;

    public InMemoryTaskRepository() {
        storage = new HashMap<>();
    }

    @Override
    public void save(Task task) {
        storage.put(task.getId(), task);
    }

    @Override
    public List<Task> findAll() {
        return storage.values().stream()
                .map(Task::copy)
                .toList();
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(storage.get(id)).map(Task::copy);
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
