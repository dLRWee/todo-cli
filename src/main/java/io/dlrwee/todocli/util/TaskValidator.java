package io.dlrwee.todocli.util;

import io.dlrwee.todocli.exception.impl.IllegalDescriptionTodoException;
import io.dlrwee.todocli.exception.impl.IllegalTitleTodoException;

public final class TaskValidator {

    private TaskValidator() {
        throw new AssertionError("Task Validator is a util class and cannot be instantiated");
    }

    public static void validateTitle(String title) {
        if (title.isBlank()) {
            throw new IllegalTitleTodoException(title);
        }
    }

    public static void validateDescription(String description) {
        if (description.isBlank()) {
            throw new IllegalDescriptionTodoException(description);
        }
    }
}
