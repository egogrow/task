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
        
        // 인원수 보다 금액이 적을 경우 예외처리
        if (money < count) {
            throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_MONEY);
        }

        // 대화방에 고유한 token이 생성될때까지 수행
        while(true) {
            // 랜덤한 영문 대소문자 3자리 문자열 생성
            token = RandomStringUtils.randomAlphanumeric(3);

            // 대화방 고유 token 검증
            am = amRepo.findByRoomIdAndToken(roomId, token);
            if (am == null) {
                break;
            } else {
                // 동일 대화방, 토큰으로 뿌리기 존재 예외처리
                throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_ALREADY);
            }
        }

        // 토큰 생성 오류 예외처리
        if (!token.isEmpty()) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_TOKEN);
        
        // 뿌리기 요청 정보 생성 저장
        am = new AllocateMoney().builder()
                .token(token)
                .roomId(roomId)
                .userId(userId)
                .money(money)
                .count(count)
                .regDate(LocalDateTime.now())
                .build();
        amRepo.save(am); // 뿌리기 요청 정보 저장
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

        // 뿌리기 금액 정보 생성
        AllocateMoneyDetail amd = null;
        List<AllocateMoneyDetail> amdList = new ArrayList<>();
        for (int i = 0; i < splitMoney.length; i++) {
            amd = new AllocateMoneyDetail().builder()
                    .token(token)
                    .roomId(roomId)
                    .receiverId(-1)
                    .splitMoney(splitMoney[i])
                    .regDate(LocalDateTime.now())
                    .build();
            amdList.add(amd);
        }
        amdRepo.saveAll(amdList); // 뿌리기 금액 정보 저장
        logger.info("amdList={}", LogUtil.printData(amdList));

        return token;
    }

    /**
     * 받기 처리 프로세스
     *
     * @param roomId
     * @param userId
     * @param token
     * @return
     */
    public int receiveMoney(String roomId, int userId, String token) {

        AllocateMoney am = null;
        AllocateMoneyDetail amd = null;
        List<AllocateMoneyDetail> amdList = null;
        LocalDateTime ldt = LocalDateTime.now().minusMinutes(10); // 현시간으로부터 10분전

        // roomId, token 값으로 뿌리기 정보 조회
        am = amRepo.findByRoomIdAndToken(roomId, token);

        // 대화방이 다르거나 token이 유효하지 않은 경우
        if (am == null) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_ROOM_TOKEN);

        // 제한시간 10분 초과로 받을 수 없습니다.
        if (ldt.isAfter(am.getRegDate())) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_10_MINUTES);

        // 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
        if (am.getUserId() == userId) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_RECEVICE);

        // roomId, token, userId 값으로 뿌리기 금액 정보 조회
        amdList = amdRepo.findByRoomIdAndTokenAndReceiverId(roomId, token, userId);

        // 조회된 결과가 없을 경우
        if (amdList == null) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_AMD_EMPTY);

        // 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
        if (amdList != null && amdList.size() > 0) {
            throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_DUPLICATE);
        }
        
        // 받기 가능한 금액 정보 조회 (-1 : 받기 미할당 값)
        int receiveMoney = 0;
        amdList = amdRepo.findByRoomIdAndTokenAndReceiverId(roomId, token, -1);
        if (amdList != null && amdList.size() > 0) {
            amd = amdList.get(0); // 첫번째 금액 정보부터 사용.
            
            amd.setReceiverId(userId); // 미할당 -1 값에서 요청자의 userId로 설정
            amdRepo.save(amd); // 기존 정보 갱신

            receiveMoney = amd.getSplitMoney();
        } else {
            // 더 이상 받을 뿌리기가 없습니다.
            throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_END);
        }

        return receiveMoney;
    }

}
