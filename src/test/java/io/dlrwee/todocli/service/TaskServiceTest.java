package io.dlrwee.todocli.service;

import io.dlrwee.todocli.common.task.TaskDescriptionValidationTests;
import io.dlrwee.todocli.common.task.TaskTitleValidationTests;
import io.dlrwee.todocli.exception.impl.NoSuchTaskTodoException;
import io.dlrwee.todocli.model.Task;
import io.dlrwee.todocli.repository.TaskRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private static final String TITLE = "Cat";
    private static final String DESCRIPTION = "Love your cat daily.";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();
    private static final boolean COMPLETED = false;

    private TaskService service;
    private TaskRepository repository;
    private ArgumentCaptor<Task> captor;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TaskRepository.class);
        service = new TaskService(repository);
        captor = ArgumentCaptor.forClass(Task.class);
    }

    @Nested
    class AddTask implements TaskTitleValidationTests, TaskDescriptionValidationTests {

        @Test
        @DisplayName("Should save task to repository with correct fields")
        void shouldSaveTaskToRepositoryWithCorrectFields() {
            service.addTask(TITLE, DESCRIPTION, CREATED_AT, COMPLETED);

            verify(repository).save(captor.capture());
            Task captured = captor.getValue();

            assertThat(captured.getId()).isNotNull();
            assertThat(captured.getCreatedAt()).isEqualTo(CREATED_AT);
            assertThat(captured.getTitle()).isEqualTo(TITLE);
            assertThat(captured.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(captured.isCompleted()).isEqualTo(COMPLETED);
        }

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithDescription(String description) {
            return () -> service.addTask(TITLE, description, CREATED_AT, COMPLETED);
        }

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithTitle(String title) {
            return () -> service.addTask(title, DESCRIPTION, CREATED_AT, COMPLETED);
        }
    }

    @Nested
    class GetAllTasksSorted {

        @Test
        @DisplayName("Should return tasks sorted by provided comparator")
        void shouldReturnTaskSortedByProvidedComparator() {
            Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
            List<Task> unsortedTasks = List.of(
                    Task.builder("b", "b").build(),
                    Task.builder("c", "c").build(),
                    Task.builder("a", "a").build()
            );
            when(repository.findAll()).thenReturn(unsortedTasks);

            List<Task> result = service.getAllTasksSorted(comparator);

            assertThat(result)
                    .hasSize(3)
                    .isSortedAccordingTo(comparator);
        }
    }

    @Nested
    class ToggleTaskCompleted {

        @ParameterizedTest(name = "was: {0}, expected: {1}")
        @CsvSource({
                "true, false",
                "false, true"
        })
        @DisplayName("Should invert completed status of existing task")
        void shouldInvertCompletedStatusOfExistingTask(boolean was, boolean expected) {
            Task task = Task.builder(TITLE, DESCRIPTION)
                    .setCompleted(was)
                    .build();
            when(repository.findById(task.getId())).thenReturn(Optional.of(task));

            service.toggleTaskStatus(task.getId());

            verify(repository).save(captor.capture());
            Task captured = captor.getValue();

            assertThat(captured.getId()).isEqualTo(task.getId());
            assertThat(captured.getCreatedAt()).isEqualTo(task.getCreatedAt());
            assertThat(captured.getTitle()).isEqualTo(task.getTitle());
            assertThat(captured.getDescription()).isEqualTo(task.getDescription());
            assertThat(captured.isCompleted()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should throw exception if task not found")
        void shouldThrowExceptionIfTaskNotFound() {
            UUID id = UUID.randomUUID();
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.toggleTaskStatus(id))
                    .isInstanceOf(NoSuchTaskTodoException.class);

            verify(repository, never()).save(any());
        }
    }

    @Nested
    class DeleteTask {

        @Test
        @DisplayName("Should invoke repository deleteById with correct UUID")
        void shouldDeleteTask() {
            UUID id = UUID.randomUUID();

            service.deleteTask(id);

            verify(repository).deleteById(id);
        }
    }

    @Nested
    class DeleteAll {

        @Test
        @DisplayName("Should invoke repository clear")
        void shouldInvokeRepositoryClear() {
            service.deleteAll();

            verify(repository).clear();
        }
    }
}
