package io.dlrwee.todocli.exception;

public class TodoException extends RuntimeException {

    public TodoException() {
        super();
    }

    public TodoException(String message) {
        super(message);
    }

    public TodoException(Throwable cause) {
        super(cause);
    }

    public TodoException(String message, Throwable cause) {
        super(message, cause);
    }
}
