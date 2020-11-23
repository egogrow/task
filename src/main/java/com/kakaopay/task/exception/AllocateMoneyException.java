package com.kakaopay.task.exception;

import com.kakaopay.task.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class AllocateMoneyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private ErrorCode data;

    public AllocateMoneyException(HttpStatus status, ErrorCode data) {
        this.status = status;
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ErrorCode getData() {
        return data;
    }

}
