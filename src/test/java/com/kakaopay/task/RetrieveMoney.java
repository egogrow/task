package com.kakaopay.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kakaopay.task.code.ErrorCode;
import com.kakaopay.task.domain.AllocateMoney;
import com.kakaopay.task.domain.AllocateMoneyRequest;
import com.kakaopay.task.repository.AllocateMoneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RetrieveMoneyTest {

    /**
     * 조회 테스트 시나리오
     *
     * retrieveMoney_param_valid : ROOM_ID, USER_ID, token 유효성 검사
     * retrieveMoney_valid_1 : 대화방이 다르거나 token이 유효하지 않은 경우 -> 검사
     * retrieveMoney_valid_2 : 자신이 신청한 뿌리기만 조회 가능합니다. -> 검사
     * retrieveMoney_valid_3 : 조회한 뿌리기는 7일이 지나 만료되었습니다. -> 검사
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

    @Autowired
    AllocateMoneyRepository amRepo;

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
    @DisplayName("조회 유효성 검사")
    void retrieveMoney_param_valid() throws Exception {
        AllocateMoneyRequest amReq = new AllocateMoneyRequest();
        amReq.setMoney(1000);
        amReq.setCount(3);

        MockHttpServletRequestBuilder builder = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "1")
                .content(mapper.writeValueAsString(amReq));

        MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        HashMap<String, String> rtnJson = new Gson().fromJson(content, HashMap.class);
        token = rtnJson.get("token");

        // ROOM_ID 유효성 예외처리 검사
        MockHttpServletRequestBuilder builder2 = get(URL+"/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "")
                .header(USER_ID, "1");

        mockMvc.perform(builder2)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getCode())));

        // USER_ID 유효성 예외처리 검사
        MockHttpServletRequestBuilder builder3 = get(URL+"/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "0");

        mockMvc.perform(builder3)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getCode())));

        // token 유효성 예외처리 검사
        MockHttpServletRequestBuilder builder4 = get(URL+"/dt")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "2");

        mockMvc.perform(builder4)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_REQUEST.getCode())));
    }

    @Test
    @DisplayName("대화방이 다르거나 token이 유효하지 않은 경우 -> 검사")
    void retrieveMoney_valid_1() throws Exception {
        AllocateMoneyRequest amReq = new AllocateMoneyRequest();
        amReq.setMoney(1000);
        amReq.setCount(3);

        MockHttpServletRequestBuilder builder = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "1")
                .content(mapper.writeValueAsString(amReq));

        MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        HashMap<String, String> rtnJson = new Gson().fromJson(content, HashMap.class);
        token = rtnJson.get("token");

        MockHttpServletRequestBuilder builder2 = get(URL+"/dtA")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "2");

        mockMvc.perform(builder2)
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_ROOM_TOKEN.getCode())));
    }

    @Test
    @DisplayName("자신이 신청한 뿌리기만 조회 가능합니다. -> 검사")
    void retrieveMoney_valid_2() throws Exception {
        AllocateMoneyRequest amReq = new AllocateMoneyRequest();
        amReq.setMoney(1000);
        amReq.setCount(3);

        MockHttpServletRequestBuilder builder = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "1")
                .content(mapper.writeValueAsString(amReq));

        MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        HashMap<String, String> rtnJson = new Gson().fromJson(content, HashMap.class);
        token = rtnJson.get("token");

        MockHttpServletRequestBuilder builder2 = get(URL+"/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "2");

        mockMvc.perform(builder2)
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_RETRIEVE.getCode())));
    }

    @Test
    @DisplayName("조회한 뿌리기는 7일이 지나 만료되었습니다. -> 검사")
    void retrieveMoney_valid_3() throws Exception {
        AllocateMoneyRequest amReq = new AllocateMoneyRequest();
        amReq.setMoney(1000);
        amReq.setCount(3);

        MockHttpServletRequestBuilder builder = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "1")
                .content(mapper.writeValueAsString(amReq));

        MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        HashMap<String, String> rtnJson = new Gson().fromJson(content, HashMap.class);
        token = rtnJson.get("token");

        // 기존 생성된 token의 정보로 조회 후 날짜를 7일전으로 변경 후 테스트 진행
        AllocateMoney am = null;
        am = amRepo.findByRoomIdAndToken("room1", token);
        if (am != null) {
            LocalDateTime lt = LocalDateTime.now().minusDays(7);
            am.setRegDate(lt);
            amRepo.save(am);
        }

        MockHttpServletRequestBuilder builder2 = get(URL+"/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(ROOM_ID, "room1")
                .header(USER_ID, "1");

        mockMvc.perform(builder2)
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.resultCode.code", is(ErrorCode.ERROR_7_DAY.getCode())));
    }

}
