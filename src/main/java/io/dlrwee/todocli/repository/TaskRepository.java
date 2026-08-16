package io.dlrwee.todocli.repository;

import io.dlrwee.todocli.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    void save(Task task);
    List<Task> findAll();
    Optional<Task> findById(UUID id);
    void deleteById(UUID id);
    void clear();
}
