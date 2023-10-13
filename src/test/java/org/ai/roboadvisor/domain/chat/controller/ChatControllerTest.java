package org.ai.roboadvisor.domain.chat.controller;

import com.google.gson.Gson;
import org.ai.roboadvisor.domain.chat.dto.request.ClearRequest;
import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatOrderResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResult;
import org.ai.roboadvisor.domain.chat.service.ChatService;
import org.ai.roboadvisor.global.config.WebConfig;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatService chatService;

    private final String TEST_USER_NICKNAME = "test_nickname";
    private final String ROLE_USER = "user";
    private final String ROLE_ASSISTANT = "assistant";
    private final String WELCOME_MESSAGE = "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요";

    /**
     * getAllChats
     */
    @Test
    @DisplayName("처음 대화방에 입장한 경우")
    void getAllChats_when_data_is_not_exists() throws Exception {
        // given
        String testNickname = TEST_USER_NICKNAME;
        LocalDateTime now = LocalDateTime.now().withNano(0);
        ChatResponse chatResponse = ChatResponse.builder()
                .role(ROLE_ASSISTANT)
                .content("welcome message")
                .time(now)
                .build();

        // when
        Mockito.when(chatService.getAllChats(testNickname)).thenReturn(new ChatResult(chatResponse));

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/{nickname}", testNickname))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(SuccessCode.WELCOME_MESSAGE_CREATED_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.WELCOME_MESSAGE_CREATED_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.chatResponse.role", is(ROLE_ASSISTANT)))
                .andExpect(jsonPath("$.data.chatResponse.role").value(ROLE_ASSISTANT))
                .andDo(print());

        verify(chatService, times(1)).getAllChats(testNickname);
    }

    @Test
    @DisplayName("db에 대화 내용 1개가 저장되어 있는 경우")
    void getMessageList_one_chat_already_saved_in_db() throws Exception {
        // given
        String testNickname = TEST_USER_NICKNAME;
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<ChatOrderResponse> responseList = Collections.singletonList(
                ChatOrderResponse.builder()
                        .order(1)
                        .role(ROLE_ASSISTANT)
                        .content("welcome message")
                        .time(now)
                        .build());

        // when
        Mockito.when(chatService.getAllChats(testNickname)).thenReturn(new ChatResult(responseList));

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/{nickname}", testNickname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(SuccessCode.LOAD_CHAT_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.LOAD_CHAT_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.chatOrderResponse[0].order").value(1))
                .andExpect(jsonPath("$.data.chatOrderResponse[0].role").value(ROLE_ASSISTANT))
                .andDo(print());

        verify(chatService, times(1)).getAllChats(testNickname);
    }

    @Test
    @DisplayName("db에 대화 내용 3개가 저장되어 있는 경우")
    void getAllChats_three_chat_already_saved_in_db() throws Exception {
        // given
        String testNickname = TEST_USER_NICKNAME;
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<ChatOrderResponse> responseList = Arrays.asList(
                ChatOrderResponse.builder()
                        .order(3)
                        .role(ROLE_ASSISTANT)
                        .content("third message!")
                        .time(now.plusSeconds(5000))
                        .build(),
                ChatOrderResponse.builder()
                        .order(2)
                        .role(ROLE_USER)
                        .content("second message!")
                        .time(now.plusSeconds(3000))
                        .build(),
                ChatOrderResponse.builder()
                        .order(1)
                        .role(ROLE_ASSISTANT)
                        .content(WELCOME_MESSAGE)
                        .time(now)
                        .build()
        );

        // when
        Mockito.when(chatService.getAllChats(testNickname)).thenReturn(new ChatResult(responseList));

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/{nickname}", testNickname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(SuccessCode.LOAD_CHAT_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.LOAD_CHAT_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.chatOrderResponse[0].order").value(3))
                .andExpect(jsonPath("$.data.chatOrderResponse[0].role").value(ROLE_ASSISTANT))
                .andExpect(jsonPath("$.data.chatOrderResponse[1].order").value(2))
                .andExpect(jsonPath("$.data.chatOrderResponse[1].role").value(ROLE_USER))
                .andExpect(jsonPath("$.data.chatOrderResponse[2].order").value(1))
                .andExpect(jsonPath("$.data.chatOrderResponse[2].role").value(ROLE_ASSISTANT))
                .andDo(print());

        verify(chatService, times(1)).getAllChats(testNickname);
    }


    /**
     * sendMessage
     */
    @Test
    @DisplayName("case success: 메시지가 정상적으로 요청 처리되며, 응답이 정상적으로 처리된다")
    void sendMessage() throws Exception {
        // given
        MessageRequest messageRequest = MessageRequest.builder()
                .nickname(TEST_USER_NICKNAME)
                .content("테스트 메시지 입니다.")
                .time("2023-08-04 04:20:19")
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(messageRequest);

        String testContent = "ChatGPT의 테스트 응답 입니다.";
        LocalDateTime now = LocalDateTime.now().withNano(0);
        ChatResponse chatResponse = ChatResponse.builder()
                .role(ROLE_ASSISTANT)
                .content(testContent)
                .time(now)
                .build();

        // when
        Mockito.doNothing().when(chatService).save(messageRequest); // return type is void
        Mockito.when(chatService.getMessageFromApi(messageRequest.getNickname(), messageRequest.getContent()))
                .thenReturn(chatResponse);

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/chat/")
                        .header("Content-Type", "application/json")
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(SuccessCode.CHAT_CREATED_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.CHAT_CREATED_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.chatResponse.role").value(ROLE_ASSISTANT))
                .andExpect(jsonPath("$.data.chatResponse.content").value(testContent))
                .andDo(print());

        verify(chatService, times(1)).save(messageRequest);
        verify(chatService, times(1)).getMessageFromApi(messageRequest.getNickname(),
                messageRequest.getContent());
    }

    @Test
    @DisplayName("case fail: MessageRequest가 정상적으로 저장되지 않는 경우 CustomException 리턴")
    void sendChatBotMessage_should_throw_CustomException_when_save_fails() throws Exception {
        // given
        MessageRequest messageRequest = MessageRequest.builder()
                .nickname(TEST_USER_NICKNAME)
                .content("테스트 메시지 입니다.")
                .time("2023-08-04 04:20:19")
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(messageRequest);

        // when
        Mockito.doThrow(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)).when(chatService).save(messageRequest);

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/chat/")
                        .header("Content-Type", "application/json")
                        .content(content))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code", is(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())))
                .andExpect(jsonPath("$.data").doesNotExist())     // check data is null
                .andDo(print());
    }

    /**
     * clear
     */
    @Test
    @DisplayName("정상적으로 db에서 데이터가 제거된 경우, 다시 Welcome Message를 리턴한다")
    void clear() throws Exception {
        // given
        ClearRequest clearRequest = ClearRequest.of(TEST_USER_NICKNAME);

        LocalDateTime now = LocalDateTime.now().withNano(0);
        ChatResponse chatResponse = ChatResponse.builder()
                .role(ROLE_ASSISTANT)
                .content(WELCOME_MESSAGE)
                .time(now)
                .build();

        ChatResult chatResult = new ChatResult(chatResponse);

        Gson gson = new Gson();
        String content = gson.toJson(clearRequest);

        // when
        Mockito.when(chatService.clear(TEST_USER_NICKNAME)).thenReturn(chatResult);

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/chat/clear")
                        .header("Content-Type", "application/json")
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(SuccessCode.CHAT_DELETED_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.CHAT_DELETED_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.chatResponse.role").value(ROLE_ASSISTANT))
                .andExpect(jsonPath("$.data.chatResponse.content").value(WELCOME_MESSAGE))
                .andDo(print());

        verify(chatService, times(1)).clear(TEST_USER_NICKNAME);
    }

    @Test
    @DisplayName("서버, db 오류로 인해 정상적으로 데이터가 지워지지 않은 경우, CustomException 리턴")
    void clear_should_throw_CustomException_when_delete_fails() throws Exception {
        // given
        ClearRequest clearRequest = ClearRequest.of(TEST_USER_NICKNAME);
        Gson gson = new Gson();
        String content = gson.toJson(clearRequest);

        // when
        Mockito.when(chatService.clear(TEST_USER_NICKNAME))
                .thenThrow(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/chat/clear")
                        .header("Content-Type", "application/json")
                        .content(content))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code", is(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())))
                .andExpect(jsonPath("$.data").doesNotExist())     // check data is null
                .andDo(print());

        verify(chatService, times(1)).clear(TEST_USER_NICKNAME);
    }
}