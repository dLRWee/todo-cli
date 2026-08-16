package io.dlrwee.todocli.exception.impl;

import io.dlrwee.todocli.exception.TodoException;

public final class IllegalDescriptionTodoException extends TodoException {
    
    public IllegalDescriptionTodoException() {
        super();
    }

    public IllegalDescriptionTodoException(String description) {
        super(createMessage(description));
    }

    public IllegalDescriptionTodoException(Throwable cause) {
        super(cause);
    }

    public IllegalDescriptionTodoException(String description, Throwable cause) {
        super(createMessage(description), cause);
    }

    private static String createMessage(String description) {
        return String.format("Illegal task description: \"%s\"", description);
    }
}