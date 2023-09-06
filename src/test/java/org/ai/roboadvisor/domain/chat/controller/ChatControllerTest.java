package org.ai.roboadvisor.domain.chat.controller;

import com.google.gson.Gson;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResponse;
import org.ai.roboadvisor.domain.chat.service.ChatService;
import org.ai.roboadvisor.global.config.WebConfig;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        })
class ChatControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatService chatService;

    private final String ROLE_USER = "user";
    private final String ROLE_ASSISTANT = "assistant";
    private final String WELCOME_MESSAGE = "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요";

    @Test
    @DisplayName("처음 대화방에 입장한 경우, 1개의 데이터가 반환되며, order의 값은 null이다")
    void getMessageList_when_data_is_not_exists() throws Exception {
        // given
        String testEmail = "test@tester.com";
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<ChatResponse> responseList = Collections.singletonList(ChatResponse.builder()
                .order(null)
                .role(ROLE_ASSISTANT)
                .content(WELCOME_MESSAGE)
                .time(now.toString())
                .build());

        // when
        Mockito.when(chatService.getChatList(testEmail)).thenReturn(responseList);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/{userEmail}", testEmail))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(SuccessCode.WELCOME_MESSAGE_CREATED_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.WELCOME_MESSAGE_CREATED_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data[0].order").doesNotExist()) // Just an example to validate order field
                .andExpect(jsonPath("$.data[0].role", is(ROLE_ASSISTANT)))
                .andExpect(jsonPath("$.data[0].content", is(WELCOME_MESSAGE)))
                .andDo(print());
    }

    @Test
    @DisplayName("db에 대화 내용 1개가 저장되어 있는 경우")
    void getMessageList_one_chat_already_saved_in_db() throws Exception {
        // given
        String testEmail = "test@tester.com";
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<ChatResponse> responseList = Collections.singletonList(ChatResponse.builder()
                .order(1)
                .role(ROLE_ASSISTANT)
                .content(WELCOME_MESSAGE)
                .time(now.toString())
                .build());

        // when
        Mockito.when(chatService.getChatList(testEmail)).thenReturn(responseList);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/{userEmail}", testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(SuccessCode.LOAD_CHAT_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.LOAD_CHAT_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data[0].order", is(1)))
                .andExpect(jsonPath("$.data[0].role", is(ROLE_ASSISTANT)))
                .andExpect(jsonPath("$.data[0].content", is(WELCOME_MESSAGE)))
                .andDo(print());
    }

    @Test
    @DisplayName("db에 대화 내용 3개가 저장되어 있는 경우")
    void getMessageList_three_chat_already_saved_in_db() throws Exception {
        // given
        String testEmail = "test@tester.com";
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<ChatResponse> responseList = Arrays.asList(
                ChatResponse.builder()
                        .order(3)
                        .role(ROLE_ASSISTANT)
                        .content("third message!")
                        .time(now.plusSeconds(5000).toString())
                        .build(),
                ChatResponse.builder()
                        .order(2)
                        .role(ROLE_USER)
                        .content("second message!")
                        .time(now.plusSeconds(3000).toString())
                        .build(),
                ChatResponse.builder()
                        .order(1)
                        .role(ROLE_ASSISTANT)
                        .content(WELCOME_MESSAGE)
                        .time(now.toString())
                        .build()
        );

        // when
        Mockito.when(chatService.getChatList(testEmail)).thenReturn(responseList);

        // given
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/{userEmail}", testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(SuccessCode.LOAD_CHAT_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.LOAD_CHAT_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data[0].order", is(3)))
                .andExpect(jsonPath("$.data[0].role", is(ROLE_ASSISTANT)))
                .andExpect(jsonPath("$.data[1].order", is(2)))
                .andExpect(jsonPath("$.data[1].role", is(ROLE_USER)))
                .andExpect(jsonPath("$.data[2].order", is(1)))
                .andExpect(jsonPath("$.data[2].role", is(ROLE_ASSISTANT)))
                .andExpect(jsonPath("$.data[2].content", is(WELCOME_MESSAGE)))
                .andDo(print());
    }

    /**
     * sendChatBotMessage(@RequestBody MessageRequest messageRequest)
     */
    @Test
    @DisplayName("MessageRequest가 정상적으로 저장되는 경우")
    void sendChatBotMessage() throws Exception {
        // given
        MessageRequest messageRequest = MessageRequest.builder()
                .email("test@test.com")
                .content("테스트 메시지 입니다.")
                .time("2023-08-04 04:20:19")
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(messageRequest);

        String testContent = "ChatGPT의 테스트 응답 입니다.";
        String testTime = "2023-08-04 04:20:30";
        Message message = Message.builder()
                .role(ROLE_ASSISTANT)
                .content(testContent)
                .time(testTime)
                .build();

        // when
        Mockito.when(chatService.saveChat(messageRequest)).thenReturn(true);
        Mockito.when(chatService.getMessageFromApi(messageRequest.getEmail(), messageRequest.getContent()))
                .thenReturn(message);

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/chat/")
                        .header("Content-Type", "application/json")
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(SuccessCode.CHAT_CREATED_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.CHAT_CREATED_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.role", is(ROLE_ASSISTANT)))
                .andExpect(jsonPath("$.data.content", is(testContent)))
                .andExpect(jsonPath("$.data.time", is(testTime)))
                .andDo(print());
    }

    @Test
    @DisplayName("MessageRequest가 정상적으로 저장되지 않는 경우 CustomException 리턴")
    void sendChatBotMessage_should_throw_CustomException_when_save_fails() throws Exception {
        // given
        MessageRequest messageRequest = MessageRequest.builder()
                .email("test@test.com")
                .content("테스트 메시지 입니다.")
                .time("2023-08-04 04:20:19")
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(messageRequest);

        // when
        Mockito.when(chatService.saveChat(messageRequest)).thenReturn(false);

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
     * clear(@PathVariable("userEmail") String email)
     */
    @Test
    @DisplayName("정상적으로 db에서 데이터가 제거된 경우, 다시 Welcome Message를 리턴한다")
    void clear() throws Exception {
        // given
        String testEmail = "test@tester.com";
        LocalDateTime now = LocalDateTime.now().withNano(0);
        ChatResponse chatResponse = ChatResponse.builder()
                .order(null)
                .role(ROLE_ASSISTANT)
                .content(WELCOME_MESSAGE)
                .time(now.toString())
                .build();

        // when
        Mockito.when(chatService.clear(testEmail)).thenReturn(true);
        Mockito.when(chatService.createAndSaveWelcomeMessage(testEmail)).thenReturn(chatResponse);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/clear/{userEmail}", testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(SuccessCode.CHAT_DELETED_SUCCESS.getCode())))
                .andExpect(jsonPath("$.message", is(SuccessCode.CHAT_DELETED_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data[0].order").doesNotExist()) // Just an example to validate order field
                .andExpect(jsonPath("$.data[0].role", is(ROLE_ASSISTANT)))
                .andExpect(jsonPath("$.data[0].content", is(WELCOME_MESSAGE)))
                .andDo(print());
    }

    @Test
    @DisplayName("서버, db 오류로 인해 정상적으로 데이터가 지워지지 않은 경우, CustomException 리턴")
    void clear_should_throw_CustomException_when_delete_fails() throws Exception {
        // given
        String testEmail = "test@tester.com";

        // when
        Mockito.when(chatService.clear(testEmail)).thenReturn(false);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/chat/clear/{userEmail}", testEmail))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code", is(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())))
                .andExpect(jsonPath("$.data").doesNotExist())     // check data is null
                .andDo(print());
    }
}