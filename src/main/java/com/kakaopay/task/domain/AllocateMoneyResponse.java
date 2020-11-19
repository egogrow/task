package com.kakaopay.task.domain;

import com.kakaopay.task.code.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocateMoneyResponse {
    private ErrorCode resultCode;
    private String token;

    @Builder
    public AllocateMoneyResponse(ErrorCode resultCode, String token) {
        this.resultCode = resultCode;
        this.token = token;
    }
}
