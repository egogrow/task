package com.kakaopay.task.service;

import com.kakaopay.task.code.ErrorCode;
import com.kakaopay.task.domain.AllocateMoney;
import com.kakaopay.task.domain.AllocateMoneyDetail;
import com.kakaopay.task.domain.AllocateMoneyRequest;
import com.kakaopay.task.exception.AllocateMoneyException;
import com.kakaopay.task.repository.AllocateMoneyDetailRepository;
import com.kakaopay.task.repository.AllocateMoneyRepository;
import com.kakaopay.task.util.LogUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AllocateMoneyService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AllocateMoneyRepository amRepo;

    @Autowired
    private AllocateMoneyDetailRepository amdRepo;

    /**
     * 뿌리기 처리 프로세스
     * 
     * @param roomId
     * @param userId
     * @param amReq
     * @return
     * @throws Exception
     */
    public String allocateMoney(String roomId, int userId, AllocateMoneyRequest amReq) throws Exception {

        AllocateMoney am = null;
        int money = amReq.getMoney();
        int count = amReq.getCount();
        String token = "";

        // 대화방에 고유한 token이 생성될때까지 수행
        while(true) {
            // 랜덤한 영문 대소문자 3자리 문자열 생성
            token = RandomStringUtils.randomAlphanumeric(3);

            // 대화방 고유 token 검증
            am = amRepo.findByRoomIdAndToken(roomId, token);
            if (am == null) break;
        }

        // 토큰 미생성 예외처리
        if (!token.isEmpty()) {
            throw new AllocateMoneyException(HttpStatus.NOT_FOUND, ErrorCode.OK);
        }
        
        // 뿌리기 요청 정보 저장
        am = new AllocateMoney().builder()
                .token(token)
                .roomId(roomId)
                .userId(userId)
                .money(money)
                .count(count)
                .regDate(LocalDateTime.now())
                .build();

        amRepo.save(am);
        logger.info("am={}", LogUtil.printData(am));

        // 뿌리기 금액 랜덤으로 분배
        int[] splitMoney = new int[count];
        logger.info("money={}, count={}", money, count);
        for (int i = 0; i < splitMoney.length; i++) {
            if(i != splitMoney.length -1) {
                splitMoney[i] = (int) (Math.random() * money + 1);
            } else {
                splitMoney[i] = money;
            }
            money -= splitMoney[i];
        }
        logger.info("splitMoney={}", LogUtil.printData(splitMoney));

        // 뿌리기 금액 정보 저장
        AllocateMoneyDetail amd = null;
        List<AllocateMoneyDetail> amdList = new ArrayList<>();
        for (int i = 0; i < splitMoney.length; i++) {
            amd = new AllocateMoneyDetail().builder()
                    .token(token)
                    .roomId(roomId)
                    .receiverId("none")
                    .splitMoney(splitMoney[i])
                    .regDate(LocalDateTime.now())
                    .build();
            amdList.add(amd);
        }
        amdRepo.saveAll(amdList);
        logger.info("amdList={}", LogUtil.printData(amdList));

        return token;
    }

}
