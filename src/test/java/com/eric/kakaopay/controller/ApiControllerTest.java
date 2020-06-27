package com.eric.kakaopay.controller;

import com.eric.kakaopay.entity.RoomUserInfo;
import com.eric.kakaopay.entity.SpreadInfo;
import com.eric.kakaopay.entity.SpreadReceiveInfo;
import com.eric.kakaopay.repository.RoomUserInfoRepository;
import com.eric.kakaopay.repository.SpreadInfoRepository;
import com.eric.kakaopay.repository.SpreadReceiveInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties = {
        "totalRoomCount=5", //방의 갯수 최소 2개 이상 필수
        "eachRoomUserCount=6" //각방의 유저는 최소 3명 이상 필수
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //@BeforeAll 정적함수 말고 일반함수로 변경하기 위해 사용**
@Slf4j
@AutoConfigureMockMvc
public class ApiControllerTest {
    @Autowired
    private RoomUserInfoRepository roomUserInfoRepository;

    @Autowired
    private SpreadInfoRepository spreadInfoRepository;

    @Autowired
    private SpreadReceiveInfoRepository spreadReceiveInfoRepository;

    @Value("${totalRoomCount}")
    private Integer totalRoomCount;
    @Value("${eachRoomUserCount}")
    private Integer eachRoomUserCount;

    @Autowired
    MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeAll()
    void BeforeAll() {
        insertDummyData();
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx).addFilters(new CharacterEncodingFilter("UTF-8", true)) // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @BeforeEach
    void beforeEach() {

    }

    private void insertDummyData() {
        RoomUserInfo ruInfo = null;
        SpreadInfo sInfo;
        long totalAmount = 10000L;
        for (int i = 1; i < totalRoomCount + 1; i++) {
            for (int j = 1; j < eachRoomUserCount + 1; j++) {
                ruInfo = roomUserInfoRepository.save(RoomUserInfo.builder().roomId("TestRoom" + i).userId(j).build());
            }
            if (i == totalRoomCount) { //마지막 방에 뿌리기 요청일시 시간을 7일 이전으로 조작
                Timestamp ts = new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(-7));
                //더미데이터 뿌리기는 각방의 마지막 사람이 뿌림
                sInfo = spreadInfoRepository.save(SpreadInfo.builder().roomUserInfoKey(ruInfo).token("TK" + i).totalAmount(totalAmount).totalUserCount(eachRoomUserCount - 1).spreadTime(ts).regDate(ts).build());
                for (int k = 1; k < eachRoomUserCount; k++) {
                    spreadReceiveInfoRepository.save(SpreadReceiveInfo.builder().roomUserInfoKey(null).spreadInfoKey(sInfo).amount(totalAmount / (eachRoomUserCount - 1)).regDate(ts).build());
                }
            } else {
                sInfo = spreadInfoRepository.save(SpreadInfo.builder().roomUserInfoKey(ruInfo).token("TK" + i).totalAmount(totalAmount).totalUserCount(eachRoomUserCount - 1).build());
                for (int k = 1; k < eachRoomUserCount; k++) {
                    spreadReceiveInfoRepository.save(SpreadReceiveInfo.builder().roomUserInfoKey(null).spreadInfoKey(sInfo).amount(totalAmount / (eachRoomUserCount - 1)).build());
                }
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("유저와 방 정보 입력 - 정상")
    void roomUserInputOK() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/roomUserInput")
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount + 1))
                .header("X-USER-ID", "1");

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    @DisplayName("유저와 방 정보 입력 - 중복")
    void roomUserInputDuplication() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/roomUserInput")
                .header("X-ROOM-ID", "TestRoom1")
                .header("X-USER-ID", "1");

        MvcResult result = mvc.perform(request)
                .andExpect(status().isInternalServerError())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    @DisplayName("유저 또는 방 헤더 정보 없을시 (공통)")
    void roomUserIsNull() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/roomUserInput")
                .header("X-ROOM-ID", "TestRoom1");

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    @DisplayName("뿌리기 - 정상")
    void spreadOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/spread")
                .header("X-ROOM-ID", "TestRoom1") //첫번째 방에서 첫번쨰 유저가 뿌리기 한 경우
                .header("X-USER-ID", "1")
                .content("{\"totalAmount\":\"100000\",\"totalUserCount\":\"" + (eachRoomUserCount - 1) + "\"}") //뿌리기 최대 인원수는 본인을 제외해야하기때문에 각방의 인원보다 1명 적음
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    @DisplayName("뿌리기 - 뿌리기 인원수를 1명보다 적게 보낸 경우")
    void spreadTooSmallSpreadCount() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/spread")
                .header("X-ROOM-ID", "TestRoom1") //첫번째 방에서 첫번쨰 유저가 뿌리기 한 경우
                .header("X-USER-ID", "1")
                .content("{\"totalAmount\":\"100000\",\"totalUserCount\":\"" + 0 + "\"}") //뿌리기 요청 인원수가 1명보다 적을 경우
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    @DisplayName("뿌리기 - 참여하지 않은 방에 뿌리기를 요청한 경우")
    void spreadBadSpreadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/spread")
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount + 1)) //더미데이터에 없는 방
                .header("X-USER-ID", eachRoomUserCount - 1)
                .content("{\"totalAmount\":\"50000\",\"totalUserCount\":\"" + (eachRoomUserCount - 1) + "\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(7)
    @DisplayName("뿌리기 - 방에 참여한 인원보다 많은 인원수를 요청한 경우")
    void spreadTooManySpreadCount() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/spread")
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1)) //더미데이터 마지막 전방
                .header("X-USER-ID", eachRoomUserCount - 1) //각방의 마지막 전 사람
                .content("{\"totalAmount\":\"50000\",\"totalUserCount\":\"" + (eachRoomUserCount + 1) + "\"}") //각방의 인원보다 1명더 추가해서 요청
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(8)
    @DisplayName("뿌리기 - 방에 참여한 인원보다 적은 금액을 요청한 경우")
    void spreadTooSmallSpreadAmount() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/spread")
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1)) //더미데이터 마지막 전방
                .header("X-USER-ID", eachRoomUserCount - 1) //각방의 마지막 전 사람
                .content("{\"totalAmount\":\"" + (eachRoomUserCount - 1) + "\",\"totalUserCount\":\"" + (eachRoomUserCount - 1) + "\"}") //요청금액이 방에 참여한 인원보다 적은경우
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(9)
    @DisplayName("받기 - 정상")
    void receiveOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/spread/TK" + (totalRoomCount - 1))
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1))
                .header("X-USER-ID", "1");

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(10)
    @DisplayName("받기 - 토큰값이 유효하지 않는 경우")
    void receiveNoneSpreadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/spread/TK" + (totalRoomCount + 1)) //더미데이터에서 존재하지 않는 토큰
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1))
                .header("X-USER-ID", "1");

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(11)
    @DisplayName("받기 - 뿌리기 토큰이 다른방의 요청인 경우")
    void receiveBadReceiveRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/spread/TK" + totalRoomCount) //더미데이터에서는 토큰넘버 방넘버와 같음
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1))
                .header("X-USER-ID", eachRoomUserCount * (totalRoomCount - 1));

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(12)
    @DisplayName("받기 - 본인이 뿌리기한건을 본인이 받는 경우")
    void receiveSelfReceiveRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/spread/TK" + (totalRoomCount - 1))
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1))
                .header("X-USER-ID", eachRoomUserCount * (totalRoomCount - 1));

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(13)
    @DisplayName("받기 - 해당 뿌리기 요청을 한번 받고 또 요청한 경우")
    void receiveAlreadyReceiveRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/spread/TK" + (totalRoomCount - 1))
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1))
                .header("X-USER-ID", eachRoomUserCount - 1);

        mvc.perform(request).andReturn();

        MvcResult result = mvc.perform(request) //같은 요청 두번 보내 마지막 응답값 확인
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(14)
    @DisplayName("받기 - 뿌리기 요청이 10분이 지난 경우")
    void receiveExpiredReceiveRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/spread/TK" + totalRoomCount) //더미데이터의 마지막 뿌리기요청은 7일전 요청시간으로 설정
                .header("X-ROOM-ID", "TestRoom" + totalRoomCount)
                .header("X-USER-ID", eachRoomUserCount - 1);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(15)
    @DisplayName("조회 - 정상")
    void inquiryOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/spread/TK" + (totalRoomCount - 1))
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1))
                .header("X-USER-ID", eachRoomUserCount);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(16)
    @DisplayName("조회 - 7일이 지난건 조회하는 경우")
    void inquiryExpiredOrNoneInquiry() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/spread/TK" + totalRoomCount)
                .header("X-ROOM-ID", "TestRoom" + totalRoomCount)
                .header("X-USER-ID", eachRoomUserCount);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }

    @Test
    @Order(17)
    @DisplayName("조회 - 본인이 뿌리기 한건이 아닌걸 조회하는 경우")
    void inquiryNotOwnerInquiry() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/spread/TK" + (totalRoomCount - 1))
                .header("X-ROOM-ID", "TestRoom" + (totalRoomCount - 1))
                .header("X-USER-ID", eachRoomUserCount - 1);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        log.debug(result.getResponse().getContentAsString());
    }
}
