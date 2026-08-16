package io.dlrwee.todocli.model;

import io.dlrwee.todocli.util.TaskValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public final class Task {

    private final UUID id;
    private final LocalDateTime createdAt;
    private String title;
    private String description;
    private boolean completed;

    private Task(
            UUID id,
            LocalDateTime createdAt,
            String title,
            String description,
            boolean completed) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public static Builder builder(String title, String description) {
        TaskValidator.validateTitle(title);
        TaskValidator.validateDescription(description);

        return new Builder(title, description);
    }

    public static final class Builder {
        // Required
        private final String title;
        private final String description;

        // Optional
        private UUID id;
        private LocalDateTime createdAt;
        private boolean completed;

        private Builder(String title, String description) {
            this.title = title;
            this.description = description;

            id = UUID.randomUUID();
            createdAt = LocalDateTime.now();
            completed = false;
        }

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Task build() {
            return new Task(
                    id,
                    createdAt,
                    title,
                    description,
                    completed
            );
        }
    }

    public static Task copy(Task other) {
        return new Task(
                other.id,
                other.createdAt,
                other.title,
                other.description,
                other.completed
        );
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        TaskValidator.validateTitle(title);
        this.title = title;
    }

    public void setDescription(String description) {
        TaskValidator.validateDescription(description);
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Task t)) {
            return false;
        }

        return id.equals(t.id) && createdAt.equals(t.createdAt) &&
                title.equals(t.title) && description.equals(t.description) &&
                completed == t.completed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, title, description, completed);
    }

    @Override
    public String toString() {
        String createdAtString = createdAt.format(
                DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.US));
        String completedString = completed ? "+" : "-";

        return String.format("[%s] [%s]: %s - %s",
                completedString, createdAtString, title, description);
    }
}
