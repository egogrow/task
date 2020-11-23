package com.kakaopay.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.task.domain.AllocateMoneyRedis;
import com.kakaopay.task.repository.AllocateMoneyRedisRepository;
import com.kakaopay.task.repository.AllocateMoneyRepository;
import com.kakaopay.task.util.LogUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class RedisTest {

    /**
     * 뿌리기 테스트 시나리오
     *
     * allocateMoney_param_valid : ROOM_ID, USER_ID, 뿌리기 금액, 뿌리기 인원수 유효성 검사
     * allocateMoney_token_valid : TOKEN 발급 검사
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ROOM_ID = "X-ROOM-ID";
    private static final String USER_ID = "X-USER-ID";
    private static final String URL = "/v1/api/allocateMoney";
    private static String token = null;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AllocateMoneyRepository amRepo;

    @Autowired
    private AllocateMoneyRedisRepository amrRepo;

    @Autowired
    private RedisTemplate redisTemplate;

    @BeforeEach
    public void initTest() {
        if(mockMvc == null) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx)
                    .addFilter(((request, response, chain) -> {
                        response.setCharacterEncoding("UTF-8");
                        chain.doFilter(request, response);
                    }))
                    .build();
        }
    }

    @Test
    @DisplayName("redis 저장 검사")
    void redis_insert_valid() throws Exception {
//        String key = "key:springboot";
//        redisTemplate.opsForValue().set(key, "Hello");
//        String value = (String)redisTemplate.opsForValue().get(key);
//        logger.info("redis 최종 결과={}", LogUtil.printData(value));

        // userId값을 가지고 캐시 처리..해보기
//
//        @Test
//        public void redisRepository() {
//            RedisEntity entity = new RedisEntity();
//            entity.setFirstname("yeoseong");
//            entity.setLastname("yoon");
//            entity.setAge(28);
//            repository.save(entity);
//            RedisEntity findEntity = repository.findByFirstname(entity.getFirstname());
//            System.out.println(findEntity.toString());
//        }

        /** mariaDB 데이터 최초 등록 **/
//        AllocateMoney am = new AllocateMoney();
//        am = new AllocateMoney().builder()
//                .token("aTa")
//                .roomId("room10")
//                .userId(1)
//                .money(1000)
//                .count(3)
//                .regDate(LocalDateTime.now())
//                .build();
//        amRepo.save(am); // 뿌리기 요청 정보 저장
        /** mariaDB 데이터 최초 등록 **/

        // 로직 개발은.. userID로 캐시 정보가 있으면 꺼내와서 비교해보고 그 다음 프로세스 진행, 그 전에 github에 제출 준비 readme 작성 완료부터

        Long id = 3L; // 요청으로 들어오는 userId라고 간주하고 테스트 진행

        Optional<AllocateMoneyRedis> amrEntity = amrRepo.findById(id);
        AllocateMoneyRedis amr = new AllocateMoneyRedis();

        if(amrEntity.isPresent()) {
            logger.info("캐시 있음");
            amr = amrEntity.get();
        } else {
            logger.info("캐시 없음");
            // 캐시 생성
            amr.setId(id);
            amr.setToken("aTa");
            amr.setRoomId("room9");
            amr.setUserId(1);
            amr.setMoney(1200);
            amr.setCount(4);
            amr.setRegDate(LocalDateTime.now());

            amrRepo.save(amr);

            AllocateMoneyRedis resultAmr = amrRepo.findById(id).get();

            logger.info("캐시가 없어 생성 함 -> redis 최종 결과={}", LogUtil.printData(resultAmr));
            logger.info("캐시가 없어 생성 함 -> roomId={}", LogUtil.printData(resultAmr.getRoomId()));
        }
    }

}
