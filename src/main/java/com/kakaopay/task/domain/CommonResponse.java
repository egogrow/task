package com.kakaopay.task.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
public class CommonResponse {
    private String returnCode;
    private String message;
    private Map<String, String> data;

    public void setMessage(String resultCode, String resultMessage) {
        this.data = new HashMap<>();
        this.data.put("resultCode", resultCode);
        this.data.put("resultCode", resultMessage);
    }
}
