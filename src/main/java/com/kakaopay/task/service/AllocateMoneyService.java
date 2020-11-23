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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AllocateMoneyService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AllocateMoneyRepository amRepo;

    @Autowired
    private AllocateMoneyDetailRepository amdRepo;
    
    /**
     * 뿌리기 프로세스
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
            token = RandomStringUtils.randomAlphabetic(3);

            // 대화방 고유 token 검증
            am = amRepo.findByRoomIdAndToken(roomId, token);
            if (am == null) break;
        }

        // 토큰 생성 오류 예외처리
        if (StringUtils.isEmpty(token)) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_TOKEN);
        
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
        logger.info("splitMoney 셔플 전 ={}", LogUtil.printData(splitMoney));

        // 분배 금액 셔플
        int s1, s2;
        int temp;
        for (int i = 0; i < splitMoney.length; i++) {
            // for문이 돌아갈때마다 바뀌는 난수 s1, s2생성
            s1 = (int) (Math.random() * count);
            s2 = (int) (Math.random() * count);
            // 변수에 배열 s1번의 값을 담기 -> swap용
            temp = splitMoney[s1];
            // s1번째 값을 s2번째 값으로 바꿔주기
            splitMoney[s1] = splitMoney[s2];
            // s2번째 값에 swap용변수(s1)번 값 담아주기
            splitMoney[s2] = temp;
        }
        logger.info("splitMoney 셔플 후 ={}", LogUtil.printData(splitMoney));

        // 뿌리기 금액 정보 생성
        AllocateMoneyDetail amd = null;
        List<AllocateMoneyDetail> amdList = new ArrayList<>();
        for (int i = 0; i < splitMoney.length; i++) {
            amd = new AllocateMoneyDetail().builder()
                    .token(token)
                    .roomId(roomId)
                    .receiverId(-1)
                    .splitMoney(splitMoney[i])
                    .maxMoneyYn("N")
                    .regDate(LocalDateTime.now())
                    .build();
            amdList.add(amd);
        }
        amdRepo.saveAll(amdList); // 뿌리기 금액 정보 저장
        logger.info("amdList={}", LogUtil.printData(amdList));

        return token;
    }

    /**
     * 받기 프로세스
     *
     * @param roomId
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    public Map<String, Object> receiveMoney(String roomId, int userId, String token) throws Exception {

        AllocateMoney am = null;
        AllocateMoneyDetail amd = null;
        List<AllocateMoneyDetail> amdList = null;
        LocalDateTime ldt = LocalDateTime.now().minusMinutes(10); // 10분전
        Map<String, Object> result = new LinkedHashMap<>();

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
            // 최고 금액 계산
            int maxMoney = 0;
            for (AllocateMoneyDetail tempAmd : amdList) {
                // 최고 금액으로 갱신
                if (maxMoney < tempAmd.getSplitMoney()) {
                    maxMoney = tempAmd.getSplitMoney();
                }
                logger.info("money={}, maxMoney={}", tempAmd.getSplitMoney(), maxMoney);
            }

            amd = amdList.get(0); // 금액 생성 순 조회 할당
            receiveMoney = amd.getSplitMoney(); // 받을 금액
            amd.setReceiverId(userId); // 미할당 -1 값에서 요청자의 userId로 설정
            logger.info("money@@@@@@@@@={}, maxMoney@@@@@@@@@={}", receiveMoney, maxMoney);

            // 최고 금액 지급 여부 조회
            boolean maxMoneyYn = amdRepo.existsByRoomIdAndTokenAndMaxMoneyYn(roomId, token, "Y");

            // 최종 응답 결과 생성
            result.put("money", receiveMoney);
            if (!maxMoneyYn) {
                if (maxMoney == receiveMoney) {
                    result.put("message", "축하합니다. 최고 금액을 받았습니다!");
                    amd.setMaxMoneyYn("Y");
                }
            }
            result.put("data", ErrorCode.OK);

            amdRepo.save(amd); // 기존 정보 갱신

        } else {
            // 더 이상 받을 뿌리기가 없습니다.
            throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_END);
        }

        return result;
    }

    /**
     * 조회 프로세스
     * 
     * @param roomId
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    public Map<String, Object> retrieveMoney(String roomId, int userId, String token) throws Exception {

        AllocateMoney am = null;
        AllocateMoneyDetail amd = null;
        List<AllocateMoneyDetail> amdList = null;
        LocalDateTime ld = LocalDateTime.now().minusDays(7); // 7일 전
        Map<String, Object> result = new LinkedHashMap<>();

        // roomId, token 값으로 뿌리기 정보 조회
        am = amRepo.findByRoomIdAndToken(roomId, token);

        // 대화방이 다르거나 token이 유효하지 않은 경우
        if (am == null) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_ROOM_TOKEN);

        // 자신이 신청한 뿌리기만 조회 가능합니다.
        if (am.getUserId() != userId) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_RETRIEVE);

        // 조회한 뿌리기는 7일이 지나 만료되었습니다.
        if (ld.isAfter(am.getRegDate())) throw new AllocateMoneyException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ERROR_7_DAY);

        // 받은 금액 정보 조회 (-1 이 아닌 정보)
        amdList = amdRepo.findByRoomIdAndTokenAndReceiverIdNot(roomId, token, -1);
        logger.info("amdList={}", LogUtil.printData(amdList));

        // 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)
        List<Map<String, Object>> amdReceiveList = new ArrayList<>();
        int sumSplitMoney = 0;
        if (amdList != null) {
            for (AllocateMoneyDetail tempAmd : amdList) {
                // 받기 완료된 금액 합산
                sumSplitMoney += tempAmd.getSplitMoney();
                // 받은 금액, 받은 사용자 아이디 결과 생성
                Map<String, Object> amdReceive = new LinkedHashMap<>();
                amdReceive.put("splitMoney", tempAmd.getSplitMoney());   // 받은 금액
                amdReceive.put("receiverId", tempAmd.getReceiverId());   // 받은 사용자 아이디
                amdReceiveList.add(amdReceive);
            }
        }
        logger.info("amdReceiveList={}", LogUtil.printData(amdReceiveList));

        // 최종 응답 결과 생성
        result.put("allocateMoneyTime", am.getRegDate());                // 뿌린 시각
        result.put("allocateMoney", am.getMoney());                      // 뿌린 금액
        result.put("receiveMoney", sumSplitMoney);                       // 받기 완료된 금액
        if (!amdReceiveList.isEmpty()) {
            result.put("receiveMoneyInfo", amdReceiveList);              // 받기 완료된 정보
        } else {
            result.put("receiveMoneyInfo", "현재 받은 사람이 없습니다.");
        }
        result.put("data", ErrorCode.OK);

        return result;
    }

}
