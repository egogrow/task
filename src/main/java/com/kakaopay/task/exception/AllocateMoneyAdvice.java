package com.kakaopay.task.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AllocateMoneyAdvice {

    @ExceptionHandler(AllocateMoneyException.class)
    public ResponseEntity<Map<String, Object>> handler(AllocateMoneyException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("resultCode", e.getResultCode());

        return new ResponseEntity<>(resBody, e.getStatus());
    }

}
