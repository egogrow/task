package com.kakaopay.task.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AllocateMoneyResponse {
    private String resultCode;
    private String resultMessage;
    private Object token;

    @Builder
    public void AllocateMoneyResponse(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
}
