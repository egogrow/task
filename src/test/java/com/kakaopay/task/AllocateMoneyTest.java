package com.kakaopay.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.task.code.ErrorCode;
import com.kakaopay.task.domain.AllocateMoneyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AllocateMoneyTest {

    /**
     * 뿌리기 테스트 시나리오
     *
     * allocateMoney_param_valid : ROOM_ID, USER_ID, 뿌리기 금액, 뿌리기 인원수 유효성 검사
     * allocateMoney_token_valid : TOKEN 발급 검사
     */

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
    @DisplayName("뿌리기 유효성 검사")
    void allocateMoney_param_valid() throws Exception {
        // 뿌리기 금액, 인원 설정
        AllocateMoneyRequest amReq = new AllocateMoneyRequest();
        amReq.setMoney(1000);
        amReq.setCount(3);

        // ROOM_ID 유효성 예외처리 검사
        MockHttpServletRequestBuilder builder = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "")
                .header(USER_ID, "1")
                .content(mapper.writeValueAsString(amReq));

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getResultCode())));

        // USER_ID 유효성 예외처리 검사
        MockHttpServletRequestBuilder builder2 = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "0")
                .content(mapper.writeValueAsString(amReq));

        mockMvc.perform(builder2)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getResultCode())));

        // 금액이 1원 이하일 경우 검사
        amReq.setMoney(0);
        amReq.setCount(3);
        MockHttpServletRequestBuilder builder3 = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "0")
                .content(mapper.writeValueAsString(amReq));

        mockMvc.perform(builder3)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getResultCode())));

        // 인원이 1명 이하일 경우 검사
        amReq.setMoney(1000);
        amReq.setCount(0);
        MockHttpServletRequestBuilder builder4 = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "0")
                .content(mapper.writeValueAsString(amReq));

        mockMvc.perform(builder4)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getResultCode())));

        // 금액이 인원수보다 작은 경우 검사
        amReq.setMoney(2);
        amReq.setCount(3);
        MockHttpServletRequestBuilder builder5 = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "0")
                .content(mapper.writeValueAsString(amReq));

        mockMvc.perform(builder5)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getResultCode())));
    }

    @Test
    @DisplayName("뿌리기 TOKEN 발급 검사")
    void allocateMoney_token_valid() throws Exception {
        // 뿌리기 금액, 인원 설정
        AllocateMoneyRequest amReq = new AllocateMoneyRequest();
        amReq.setMoney(1000);
        amReq.setCount(3);

        // ROOM_ID 유효성 예외처리 검사
        MockHttpServletRequestBuilder builder = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "1")
                .content(mapper.writeValueAsString(amReq));

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.OK.getResultCode())));
    }

}
