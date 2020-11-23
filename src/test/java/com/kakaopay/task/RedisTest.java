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

        String token = "abc";
        String roomId = "room2";

        AllocateMoneyRedis amr = new AllocateMoneyRedis();
        amr.setToken(token);
        amr.setRoomId(roomId);
        amr.setUserId(1);
        amr.setMoney(1200);
        amr.setCount(4);
        amr.setRegDate(LocalDateTime.now());

        AllocateMoneyRedis amrEntity = amrRepo.findByRoomIdAndToken(roomId, token);
        logger.info("사전 검증 -> redis 최종 결과={}", LogUtil.printData(amrEntity));

        if(amrEntity != null) {
            logger.info("캐시 있음");
        } else {
            logger.info("캐시 없음");
        }

//        String key = "key:springboot";
//        redisTemplate.opsForValue().set(key, "Hello");
//        String value = (String)redisTemplate.opsForValue().get(key);
//        logger.info("redis 최종 결과={}", LogUtil.printData(value));
    }

}
