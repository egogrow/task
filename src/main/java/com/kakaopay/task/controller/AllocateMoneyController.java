package com.kakaopay.task.controller;

import com.kakaopay.task.constant.AllocateMoneyConstant;
import com.kakaopay.task.domain.AllocateMoneyRequest;
import com.kakaopay.task.domain.AllocateMoneyResponse;
import com.kakaopay.task.service.AllocateMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/payapi/")
public class AllocateMoneyController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    static final String ROOM_ID = "X-ROOM-ID";
    static final String USER_ID = "X-USER-ID";

    @Autowired
    AllocateMoneyService allocateMoneyService;

    /**
     * 뿌리기 API
     *
     * @param userId
     * @param roomId
     * @param amReq
     * @return
     */
    @RequestMapping(value = "/allocateMoney", method = RequestMethod.POST)
    private ResponseEntity<AllocateMoneyResponse> allocateMoney(@RequestHeader(ROOM_ID) String roomId,
                                                                @RequestHeader(USER_ID)int userId,
                                                                @RequestBody AllocateMoneyRequest amReq) throws Exception {
        String functionName = "amountDistribution";

        logger.info("[{}] 함수시작", functionName);

        AllocateMoneyResponse amRes = new AllocateMoneyResponse();

        // userId, roomId valid
        if ("".equals(roomId) || userId < 0) {
            amRes.builder()
                    .resultCode(AllocateMoneyConstant.RESULT_FAIL_HEADER)
                    .resultMessage(AllocateMoneyConstant.RESULT_FAIL_HEADER_MASSGE)
                    .build();
        }

        // AllocateMoneyRequest valid
        if (amReq.getMoney() <= 0 || amReq.getCount() <= 0) {
            amRes.builder()
                    .resultCode(AllocateMoneyConstant.RESULT_FAIL_HEADER)
                    .resultMessage(AllocateMoneyConstant.RESULT_FAIL_HEADER_MASSGE)
                    .build();
        }
        
        // url 검증, 숫자 문자 검증 추가 필요
        
        // 뿌리기 프로세스
        String token = allocateMoneyService.allocateMoney(roomId, userId, amReq);

        logger.info("[{}] 함수종료", functionName);

        return new ResponseEntity<AllocateMoneyResponse>(amRes, HttpStatus.OK);
    }

    @RequestMapping(value = "/receiveMoney", method = RequestMethod.PUT)
    private ResponseEntity<AllocateMoneyResponse> receiveMoney() {
        return null;
    }

    @RequestMapping(value = "/retrieveMoney", method = RequestMethod.GET)
    private ResponseEntity<AllocateMoneyResponse> retrieveMoney() {
        return null;
    }

}