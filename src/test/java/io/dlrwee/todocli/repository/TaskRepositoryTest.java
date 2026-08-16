package io.dlrwee.todocli.repository;

import io.dlrwee.todocli.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public abstract class TaskRepositoryTest<R extends TaskRepository> {

    protected abstract R createRepository();

    private R repository;
    private Task first;
    private Task second;

    @BeforeEach
    void setUp() {
        repository = createRepository();
        first = Task.builder("Respect", "Be respectful.").build();
        second = Task.builder("Cat", "Love your cat.").setCompleted(true).build();
        second.setCompleted(true);
    }

    @Nested
    class Save {

        @Test
        @DisplayName("Should save task if repository is empty")
        void shouldSaveTaskIfRepositoryIsEmpty() {
            repository.save(first);

            assertThat(repository.findAll()).containsExactly(first);
        }

        @Test
        @DisplayName("Should save multiple tasks")
        void shouldSaveMultipleTasks() {
            repository.save(first);
            repository.save(second);

            assertThat(repository.findAll()).containsExactlyInAnyOrder(first, second);
        }

        @Test
        @DisplayName("Should update task if it is already present")
        void shouldUpdateTaskIfItIsAlreadyPresent() {
            repository.save(first);

            Task updated = Task.copy(first);
            updated.setTitle("New title");
            repository.save(first);

            assertThat(repository.findAll())
                    .hasSize(1)
                    .first()
                    .usingRecursiveComparison()
                    .isEqualTo(first);
        }
    }

    @Nested
    class FindAll {

        @Test
        @DisplayName("Should return empty list if no tasks")
        void shouldReturnEmptyListIfNoTasks() {
            assertThat(repository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("Should return list of single task if no more tasks")
        void shouldReturnListOfSingleTaskIfNoMoreTasks() {
            repository.save(first);

            assertThat(repository.findAll()).containsExactly(first);
        }

        @Test
        @DisplayName("Should return multiple tasks in any order")
        void shouldReturnMultipleTasksInAnyOrder() {
            repository.save(first);
            repository.save(second);

            assertThat(repository.findAll()).containsExactlyInAnyOrder(first, second);
        }

        @Test
        @DisplayName("Should return copies")
        void shouldReturnCopies() {
            repository.save(first);

            assertThat(repository.findAll())
                    .hasSize(1)
                    .first()
                    .isNotSameAs(first)
                    .usingRecursiveComparison()
                    .isEqualTo(first);
        }
    }

    @Nested
    class FindById {

        @Test
        @DisplayName("Should return optional of task if it is present")
        void shouldReturnOptionalIfTaskIfItIsPresent() {
            repository.save(first);

            Optional<Task> got = repository.findById(first.getId());

            assertThat(got).contains(first);
        }

        @Test
        @DisplayName("Should return empty optional if no such task")
        void shouldReturnEmptyOptionalIfNoSuchTask() {
            repository.save(first);

            Optional<Task> got = repository.findById(second.getId());

            assertThat(got).isEmpty();
        }

        @Test
        @DisplayName("Should return copy if present")
        void shouldReturnCopyIfPresent() {
            repository.save(first);

            assertThat(repository.findById(first.getId()))
                    .get()
                    .isNotSameAs(first)
                    .usingRecursiveComparison()
                    .isEqualTo(first);
        }
    }

    @Nested
    class DeleteById {

        @Test
        @DisplayName("Should delete task if it is present")
        void shouldDeleteTaskIfItIsPresent() {
            repository.save(first);

            repository.deleteById(first.getId());

            assertThat(repository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("Should do nothing if task is not present")
        void shouldDoNothingIfTaskIsNotPresent() {
            assertThatNoException().isThrownBy(() -> repository.deleteById(first.getId()));
            assertThat(repository.findAll()).isEmpty();
        }
    }

    @Nested
    class Clear {

        @Test
        @DisplayName("Should do nothing if no tasks")
        void shouldDoNothingIfNoTasks() {
            assertThatNoException().isThrownBy(() -> repository.clear());
            assertThat(repository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("Should delete all tasks")
        void shouldDeleteAllTasks() {
            repository.save(first);
            repository.save(second);

            repository.clear();

            assertThat(repository.findAll()).isEmpty();
        }
    }
}
