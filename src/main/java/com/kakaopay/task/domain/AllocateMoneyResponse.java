package com.kakaopay.task.domain;

import com.kakaopay.task.code.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocateMoneyResponse {
    private String token;
    private ErrorCode data;

    @Builder
    public AllocateMoneyResponse(String token, ErrorCode data) {
        this.token = token;
        this.data = data;
    }
}
