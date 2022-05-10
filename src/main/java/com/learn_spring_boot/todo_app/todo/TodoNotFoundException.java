package com.learn_spring_boot.todo_app.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super();
    }

    public TodoNotFoundException(String message) {
        super(message);
    }

    public TodoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoNotFoundException(Throwable cause) {
        super(cause);
    }

    protected TodoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
