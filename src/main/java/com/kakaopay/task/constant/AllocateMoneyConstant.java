package com.kakaopay.task.constant;

public class AllocateMoneyConstant {

    //	요청 공통 응답 코드 (1000 번대)
    public static final String RESULT_SUCESS = "0000";
    public static final String RESULT_SUCESS_MASSGE = "정상 처리되었습니다.";

    public static final String RESULT_FAIL_HEADER = "1001";
    public static final String RESULT_FAIL_HEADER_MASSGE = "잘못된 Request Header 요청입니다.";

    public static final String RESULT_FAIL_PARAM = "1002";
    public static final String RESULT_FAIL_PARAM_MASSGE = "잘못된 Request Parameter 요청입니다.";

    public static final String RESULT_FAIL_TIME_OUT = "1003";
    public static final String RESULT_FAIL_TIME_OUT_MASSGE = "요청 시간이 초과되었습니다.";

    // 뿌리기 API 응답 코드 (2000 번대)
    public static final String RESULT_FAIL_ALREADY = "2001";
    public static final String RESULT_FAIL_ALREADY_MESSAGE = "이미 신청한 뿌리기가 진행중에 있습니다.";

    // 받기 API 응답 코드 (3000 번대)
    public static final String RESULT_FAIL_DUPLICATE = "3001";
    public static final String RESULT_FAIL_DUPLICATE_MESSAGE = "뿌리기 당 한 사용자는 한번만 받을 수 있습니다.";

    public static final String RESULT_FAIL_SELF = "3002";
    public static final String RESULT_FAIL_SELF_MESSAGE = "자신이 뿌리기한 건은 자신이 받을 수 없습니다.";

    public static final String RESULT_FAIL_ROOM = "3003";
    public static final String RESULT_FAIL_ROOM_MESSAGE = "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.";

    public static final String RESULT_FAIL_10_MINUTES = "3004";
    public static final String RESULT_FAIL_10_MINUTES_MESSAGE = "해당 뿌리기는 10분이 초과되어 만료되었습니다.";

    public static final String RESULT_FAIL_END = "3005";
    public static final String RESULT_FAIL_END_MESSAGE = "모든 사용자가 뿌리기를 받아 종료되었습니다.";

    // 조회 API 응답 코드 (4000 번대)
    public static final String RESULT_FAIL_TOKEN = "4001";
    public static final String RESULT_FAIL_TOKEN_MESSAGE = "유효하지 않은 token 입니다.";

    public static final String RESULT_FAIL_RETRIEVE = "4002";
    public static final String RESULT_FAIL_RETRIEVE_MESSAGE = "자신이 신청한 뿌리기만 조회 가능합니다.";

    public static final String RESULT_FAIL_7_DAY = "4003";
    public static final String RESULT_FAIL_7_DAY_MESSAGE = "조회한 뿌리기는 7일이 지나 만료되었습니다.";

    // 시스템 공통 응답 코드 (9000 번대)
    public static final String RESULT_FAIL_ERROR = "9000";
    public static final String RESULT_FAIL_ERROR_MASSGE = "예상치 않은 오류가 발생하였습니다.";

    public static final String RESULT_FAIL_DB = "9001";
    public static final String RESULT_FAIL_DB_MASSGE = "데이터베이스에 오류가 있습니다. 잠시 후 다시 시도해 주세요.";

    public static final String RESULT_FAIL_SYSTEM = "9002";
    public static final String RESULT_FAIL_SYSTEM_MASSGE = "시스템 내부에 오류가 있습니다. 잠시 후 다시 시도해 주세요.";

}
