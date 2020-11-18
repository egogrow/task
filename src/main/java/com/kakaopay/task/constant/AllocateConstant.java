package com.kakaopay.task.constant;

public class AllocateConstant {

    //	요청 공통 코드
    static final String RESULT_SUCESS = "0000";
    static final String RESULT_SUCESS_MASSGE = "정상 처리되었습니다.";

    static final String RESULT_FAIL_PARAM = "1001";
    static final String RESULT_FAIL_PARAM_MASSGE = "잘못된 Request 요청입니다.";

    static final String RESULT_FAIL_TIME_OUT = "1002";
    static final String RESULT_FAIL_TIME_OUT_MASSGE = "요청 시간이 초과되었습니다.";

//-- 뿌리기
//    이미 신청한 뿌리기가 진행중에 있습니다.
//
//-- 받기
//    뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
//    자신이 뿌리기한 건은 자신이 받을 수 없습니다.
//    뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
//            10분이 지나 뿌리기가 만료되었습니다.
//
//            -- 조회
//    모든 뿌리기가 끝났습니다
//
//    요청하신 토큰은 만료되었습니다.
//
//
//
//10분이 지나 만료되었습니다.
    // 뿌리기 코드
//    static final String RESULT_FAIL_PARAM = "1001";
//    static final String RESULT_FAIL_PARAM = "1002";

    // 받기 코드
//    static final String RESULT_FAIL_PARAM = "1003";
//    static final String RESULT_FAIL_PARAM = "1004";

    // 조회 코드
//    static final String RESULT_FAIL_PARAM = "1005";
//    static final String RESULT_FAIL_PARAM = "1006";

    // 시스템 공통 코드
    static final String RESULT_FAIL = "9000";
    static final String RESULT_FAIL_MASSGE = "예상치 않은 오류가 발생하였습니다.";

    static final String RESULT_FAIL_DB = "9001";
    static final String RESULT_FAIL_DB_MASSGE = "데이터베이스에 오류가 있습니다. 잠시 후 다시 시도해 주세요.";

    static final String RESULT_FAIL_SYSTEM = "9002";
    static final String RESULT_FAIL_SYSTEM_MASSGE = "시스템 내부에 오류가 있습니다. 잠시 후 다시 시도해 주세요.";

}
