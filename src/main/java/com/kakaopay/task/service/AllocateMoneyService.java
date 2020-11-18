package com.kakaopay.task.service;

import com.kakaopay.task.domain.AllocateMoney;
import com.kakaopay.task.domain.AllocateMoneyRequest;
import com.kakaopay.task.repository.AllocateMoneyRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllocateMoneyService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AllocateMoneyRepository AllocateRepo;

    public String allocateMoney(String roomId, int userId, AllocateMoneyRequest amReq) {
        String token = "";
        
        // 대화방에 고유한 token이 생성될때까지 수행
        while(true) {
            // 랜덤한 영문 대소문자 3자리 문자열 생성
            token = RandomStringUtils.randomAlphanumeric(3);

            // 대화방 고유 token 검증
            AllocateMoney allocateMoney = null;
            allocateMoney = AllocateRepo.findByRoomIdAndToken(roomId, token);
            if (allocateMoney == null) break;
        }

        // 뿌릴 금액 인원수에 맞춰 분배 후 저장 처리
        List<AllocateMoney> allocateMoneyLst = new ArrayList<>();
        int money = amReq.getMoney();
        int count = amReq.getCount();
        int splitMoney = 0;
        int remainder = 0;

        splitMoney = money / count;
        remainder = money % count;
        logger.info("money={}, count={}", money, count);
        for (int i = 0; i < count; i++) {
            logger.info("i={}, money={}, count={}", i, money, count);
            // 금액 % 인원수
            if (i == 0) {

            } else {

            }

        }
        logger.info("AllocateMoney={}", splitMoney);


        //리스트로 최종 담아야 함.





        //
//        logger.info("token={}", token);

        //

        return token;
    }



}
