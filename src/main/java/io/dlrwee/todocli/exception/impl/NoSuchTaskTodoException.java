package io.dlrwee.todocli.exception.impl;

import io.dlrwee.todocli.exception.TodoException;

import java.util.UUID;

public class NoSuchTaskTodoException extends TodoException {

    public NoSuchTaskTodoException() {
        super();
    }

    public NoSuchTaskTodoException(UUID id) {
        super(createMessage(id));
    }

    public NoSuchTaskTodoException(Throwable cause) {
        super(cause);
    }

    public NoSuchTaskTodoException(UUID id, Throwable cause) {
        super(createMessage(id), cause);
    }

    private static String createMessage(UUID id) {
        return String.format("No such task with id: %s", id);
    }
}
