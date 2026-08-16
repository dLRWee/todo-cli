package io.dlrwee.todocli.model;

import io.dlrwee.todocli.common.task.TaskDescriptionValidationTests;
import io.dlrwee.todocli.common.task.TaskTitleValidationTests;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class TaskTest {

    private static final String TITLE = "Gym";
    private static final String DESCRIPTION = "Exercise at least 3 times a week.";

    @Nested
    class Builder implements TaskTitleValidationTests, TaskDescriptionValidationTests {

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithDescription(String description) {
            return () -> Task.builder(TITLE, description);
        }

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithTitle(String title) {
            return () -> Task.builder(title, DESCRIPTION);
        }

        @Test
        @DisplayName("Should initialize all optional fields")
        void shouldInitializeAllOptionalFields() {
            String title = "Title";
            String description = "Description";

            Task task = Task.builder(title, description).build();

            assertThat(task.getId()).isNotNull();
            assertThat(task.getCreatedAt())
                    .isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
            assertThat(task.isCompleted()).isFalse();
        }

        @Test
        @DisplayName("Should initialize all specified fields")
        void shouldInitializeOptionalFieldsWithDefaultValues() {
            UUID id = UUID.randomUUID();
            LocalDateTime createdAt = LocalDateTime.now();
            String title = "Title";
            String description = "Description";
            boolean completed = false;

            Task task = Task
                    .builder(title, description)
                    .setId(id)
                    .setCreatedAt(createdAt)
                    .setCompleted(completed)
                    .build();

            assertThat(task.getId()).isEqualTo(id);
            assertThat(task.getCreatedAt()).isEqualTo(createdAt);
            assertThat(task.getTitle()).isEqualTo(title);
            assertThat(task.getDescription()).isEqualTo(description);
            assertThat(task.isCompleted()).isEqualTo(completed);
        }
    }


    @Nested
    class Copy {


        @Test
        @DisplayName("Should return not the same object")
        void shouldReturnNotTheSameObject() {
            Task task = Task.builder(TITLE, DESCRIPTION).build();

            Task got = Task.copy(task);

            assertThat(got).isNotSameAs(task);
        }

        @Test
        @DisplayName("Should be equal by all fields")
        void shouldBeEqualByAllFields() {
            Task task = Task.builder(TITLE, DESCRIPTION).build();

            Task got = Task.copy(task);

            assertThat(got)
                    .usingRecursiveComparison()
                    .isEqualTo(task);
        }
    }

    @Nested
    class SetTitle implements TaskTitleValidationTests {

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithTitle(String title) {
            return () -> Task.builder(TITLE, DESCRIPTION).build()
                    .setTitle(title);
        }
    }

    @Nested
    class SetDescription implements TaskDescriptionValidationTests {

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithDescription(String description) {
            return () -> Task.builder(TITLE, DESCRIPTION).build()
                    .setDescription(description);
        }
    }
}
