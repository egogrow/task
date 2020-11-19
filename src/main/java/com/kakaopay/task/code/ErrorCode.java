package com.kakaopay.task.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    OK("0000", "정상 처리되었습니다."),
    ERROR_REQUEST("1001", "잘못된 요청입니다."),
    ERROR_TIME_OUT("1002", "요청 시간이 초과되었습니다."),
    ERROR_TOKEN("1003", "토큰 생성이 실패했습니다. 잠시 후 다시 시도해 주세요."), // 처리 완료
    ERROR_MONEY("1004", "인원수보다 많은 금액을 입력하세요."), // 처리 완료
    ERROR_ALREADY("1005", "이미 신청한 뿌리기가 진행중에 있습니다."),
    ERROR_AMD_EMPTY("1006", "뿌리기 금액 상세 정보가 없습니다."), // 처리 완료
    ERROR_DUPLICATE("1007", "뿌리기 당 한 사용자는 한번만 받을 수 있습니다."), // 처리 완료
    ERROR_RECEVICE("1008", "자신이 뿌리기한 건은 자신이 받을 수 없습니다."), // 처리 완료
    ERROR_ROOM_TOKEN("1009", "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."), // 처리 완료
    ERROR_10_MINUTES("1010", "제한시간 10분 초과로 받을 수 없습니다."), // 처리 완료
    ERROR_END("1011", "더 이상 받을 뿌리기가 없습니다."), // 처리 완료
    ERROR_RETRIEVE("1012", "자신이 신청한 뿌리기만 조회 가능합니다."),
    ERROR_7_DAY("1013", "조회한 뿌리기는 7일이 지나 만료되었습니다."),
    ERROR("9000", "예상치 않은 오류가 발생하였습니다."),
    ERROR_DB("9001", "데이터베이스에 오류가 있습니다. 잠시 후 다시 시도해 주세요.")


    ;

    private final String code;
    private final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

}
