package com.kakaopay.task.controller;

import com.kakaopay.task.domain.AllocateMoneyRequest;
import com.kakaopay.task.domain.AllocateMoneyResponse;
import com.kakaopay.task.service.AllocateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/payapi/")
public class AllocateController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AllocateService allocateService;

    /**
     * 뿌리기 API
     *
     * @param userId
     * @param roomId
     * @param req
     * @return
     */
    @RequestMapping(value = "/allocateMoney", method = RequestMethod.POST)
    private ResponseEntity<AllocateMoneyResponse> allocateMoney(@RequestHeader(name = "X-USER-ID") int userId,
                                                                @RequestHeader(name = "X-ROOM-ID") String roomId,
                                                                @RequestBody AllocateMoneyRequest req) {
        String functionName = "amountDistribution";
        logger.info("[{}] 함수시작", functionName);

        AllocateMoneyResponse amRes = new AllocateMoneyResponse();

        // userId, roomId
        if ("".equals(roomId) || userId < 0) {
//            amRes.set
        }

//        allocateService

        logger.info("[{}] 함수종료", functionName);

        return null;
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
