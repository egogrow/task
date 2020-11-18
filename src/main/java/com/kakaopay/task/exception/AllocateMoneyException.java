package com.kakaopay.task.exception;

import org.springframework.http.HttpStatus;

public class AllocateMoneyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
    private HttpStatus httpStatus;

    public AllocateMoneyException(String message) {
        this(message, HttpStatus.EXPECTATION_FAILED);
    }

    public AllocateMoneyException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
