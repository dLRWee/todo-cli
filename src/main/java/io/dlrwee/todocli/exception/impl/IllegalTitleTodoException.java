package io.dlrwee.todocli.exception.impl;

import io.dlrwee.todocli.exception.TodoException;

public final class IllegalTitleTodoException extends TodoException {

    public IllegalTitleTodoException() {
        super();
    }

    public IllegalTitleTodoException(String title) {
        super(createMessage(title));
    }

    public IllegalTitleTodoException(Throwable cause) {
        super(cause);
    }

    public IllegalTitleTodoException(String title, Throwable cause) {
        super(createMessage(title), cause);
    }

    private static String createMessage(String title) {
        return String.format("Illegal task title: \"%s\"", title);
    }
}
