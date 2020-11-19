package com.kakaopay.task.controller;

import com.kakaopay.task.code.ErrorCode;
import com.kakaopay.task.domain.AllocateMoneyRequest;
import com.kakaopay.task.domain.AllocateMoneyResponse;
import com.kakaopay.task.exception.AllocateMoneyException;
import com.kakaopay.task.service.AllocateMoneyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
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
    @PostMapping("/allocateMoney")
    private AllocateMoneyResponse allocateMoney(@RequestHeader(ROOM_ID) String roomId,
                                                @RequestHeader(USER_ID)int userId,
                                                @RequestBody AllocateMoneyRequest amReq) throws Exception {

        String functionName = "amountDistribution";
        logger.info("[{}] 함수시작", functionName);

        // 요청 유효성 검증
        if (StringUtils.isEmpty(roomId) || userId <= 0 || amReq.getMoney() <= 0 || amReq.getCount() <= 0) throw new AllocateMoneyException(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REQUEST);

        // 뿌리기 프로세스 처리 후 응답 결과 생성
        AllocateMoneyResponse amRes = AllocateMoneyResponse.builder()
                .resultCode(ErrorCode.OK)
                .token(allocateMoneyService.allocateMoney(roomId, userId, amReq))
                .build();

        logger.info("[{}] 함수종료", functionName);

        return amRes;
    }

    @PutMapping("/allocateMoney/{token}")
    private ResponseEntity<Object> receiveMoney(@RequestHeader(ROOM_ID) String roomId,
                                        @RequestHeader(USER_ID) int userId,
                                        @PathVariable(name = "token") String token) throws Exception {

        String functionName = "receiveMoney";
        logger.info("[{}] 함수시작", functionName);

        // 요청 유효성 검증
        if (StringUtils.isEmpty(roomId) || userId <= 0 || StringUtils.isEmpty(token)) throw new AllocateMoneyException(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REQUEST);
        
        // 받기 프로세스 처리 후 응답 결과 생성
        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", ErrorCode.OK);
        result.put("money", allocateMoneyService.receiveMoney(roomId, userId, token));

        logger.info("[{}] 함수종료", functionName);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/allocateMoney/{token}")
    private AllocateMoneyResponse retrieveMoney(@RequestHeader(ROOM_ID) String roomId,
                                                @RequestHeader(USER_ID) int userId,
                                                @PathVariable(name = "token") String token) throws Exception {

        String functionName = "retrieveMoney";
        logger.info("[{}] 함수시작", functionName);

        // 요청 유효성 검증
        if (StringUtils.isEmpty(roomId) || userId <= 0 || StringUtils.isEmpty(token)) throw new AllocateMoneyException(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REQUEST);


        logger.info("[{}] 함수종료", functionName);

        return null;
    }

}
