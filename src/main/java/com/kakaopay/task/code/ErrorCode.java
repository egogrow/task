package com.kakaopay.task.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    OK("0000", "정상 처리되었습니다."),
    ERROR("err00", "예상치 않은 오류가 발생하였습니다."),
    INVALID_REQUEST("1001", "잘못된 요청입니다."),
    MORE_MONEY("err02", "인원수보다 많은 금액을 입력하세요.")
//    INVALID_USER("err03", "사용자 정보가 없습니다."),
//    CANNOT_DUPLICATE("err04", "중복해서 받을 수 없습니다.")
    ;

    private final String code;
    private final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

}
