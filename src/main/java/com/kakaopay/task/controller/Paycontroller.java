package com.kakaopay.task.controller;

import com.kakaopay.task.domain.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payapi")
public class Paycontroller {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 뿌리기 API
     * @param userId
     * @param roomId
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping(value = {"distribution"}, method = {RequestMethod.POST})
    private ResponseEntity<CommonResponse> amountDistribution(@RequestHeader(ROOM_ID) String roomId,
                                                              @RequestHeader(USER_ID) int masterUserId,
                                                              @RequestBody SetDistributeData rcvData) {
        String functionName = "moneyDistribution";

        return null;
    }

    //받기 API
    private ResponseEntity<CommonResponse> moneyDistribution() {
        return null;
    }

    //조회 API

}
