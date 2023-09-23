package org.ai.roboadvisor.domain.chat.service;

import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatOrderResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResult;
import org.ai.roboadvisor.domain.chat.entity.Chat;
import org.ai.roboadvisor.domain.chat.repository.ChatRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


//@Transactional // MongoDB does not support @Transactional
@SpringBootTest
@ActiveProfiles("test")
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    private final String TEST_USER_NICKNAME = "test_nickname";
    private final String ROLE_USER = "user";
    private final String ROLE_ASSISTANT = "assistant";
    private final String WELCOME_MESSAGE = "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        // delete mongodb manually
        chatRepository.deleteAll();
    }

    /**
     * getAllChats
     */
    @Test
    @DisplayName("case 1: db에 대화내용이 존재하지 않는 경우, Welcome Message를 Controller로 전달한다")
    void getAllChats_when_data_is_null() {
        // given
        String testNickname = TEST_USER_NICKNAME;

        // when
        ChatResult chatResult = chatService.getAllChats(testNickname);

        // then
        assertThat(chatResult.getChatOrderResponse()).isNull();
        assertThat(chatResult.getChatResponse()).isNotNull();

        ChatResponse result = chatResult.getChatResponse();
        assertThat(result.getRole()).isEqualTo(ROLE_ASSISTANT);
        assertThat(result.getContent()).isEqualTo(WELCOME_MESSAGE);
        assertThat(result.getTime()).isNotNull();
    }

    @Test
    @DisplayName("case 2: db에 대화내용이 존재하는 경우-대화 내용이 한 개 존재")
    void getChatList_when_data_already_exists_in_db() {
        // given
        String testNickname = TEST_USER_NICKNAME;

        LocalDateTime now = LocalDateTime.now();
        List<Chat> chats = Collections.singletonList(
                Chat.builder()
                        .nickname(testNickname)
                        .role(ROLE_ASSISTANT)
                        .message(WELCOME_MESSAGE)
                        .time(now)
                        .build()

        );
        chatRepository.saveAll(chats);

        // when
        ChatResult chatResult = chatService.getAllChats(testNickname);

        // then
        assertThat(chatResult.getChatOrderResponse()).isNotNull();
        assertThat(chatResult.getChatResponse()).isNull();

        List<ChatOrderResponse> chatOrderResponses = chatResult.getChatOrderResponse();
        assertThat(chatOrderResponses.size()).isEqualTo(1);

        ChatOrderResponse oneOrderResponse = chatOrderResponses.get(0);

        assertThat(oneOrderResponse.getOrder()).isEqualTo(1);
        assertThat(oneOrderResponse.getRole()).isEqualTo(ROLE_ASSISTANT);
        assertThat(oneOrderResponse.getContent()).isEqualTo(WELCOME_MESSAGE);
    }

    @Test
    @DisplayName("case 2-1: db에 대화내용이 존재하는 경우-대화 내용이 여러 개 존재")
    void getChatList_when_datas_already_exists_in_db() {
        // given
        String testNickname = TEST_USER_NICKNAME;

        LocalDateTime now = LocalDateTime.now();
        List<Chat> chats = Arrays.asList(
                Chat.builder()
                        .nickname(testNickname)
                        .role(ROLE_ASSISTANT)
                        .message(WELCOME_MESSAGE)
                        .time(now)
                        .build(),
                Chat.builder()
                        .nickname(testNickname)
                        .role(ROLE_USER)
                        .message("주식시장에서, 선물 거래의 의미는 무엇이니?")
                        .time(now.plusSeconds(3000))
                        .build(),
                Chat.builder()
                        .nickname(testNickname)
                        .role(ROLE_ASSISTANT)
                        .message("선물이란 .. 입니다.")
                        .time(now.plusSeconds(6000))
                        .build()
        );
        chatRepository.saveAll(chats);

        // when
        ChatResult chatResult = chatService.getAllChats(testNickname);

        // then
        assertThat(chatResult.getChatOrderResponse()).isNotNull();
        assertThat(chatResult.getChatResponse()).isNull();

        List<ChatOrderResponse> chatOrderResponses = chatResult.getChatOrderResponse();
        assertThat(chatOrderResponses.size()).isEqualTo(3);

        // Order 1, 2, 3이 정상적으로 리턴되는지 확인
        List<Integer> orderList = Arrays.asList(chatOrderResponses.get(0).getOrder(),
                chatOrderResponses.get(1).getOrder(),
                chatOrderResponses.get(2).getOrder());
        boolean order1Exists = orderList.stream().anyMatch(
                num -> num == 1);
        boolean order2Exists = orderList.stream().anyMatch(
                num -> num == 2);
        boolean order3Exists = orderList.stream().anyMatch(
                num -> num == 3);
        boolean order4Exists = orderList.stream().anyMatch(
                num -> num == 4);
        assertThat(order1Exists).isTrue();
        assertThat(order2Exists).isTrue();
        assertThat(order3Exists).isTrue();
        assertThat(order4Exists).isFalse(); // order 4 not exists because of the number of data is 3

        // 시간 순으로 order 값이 잘 정렬되었는지 확인
        // order 값이 높을 수록, 최신 메시지이다.
        ChatOrderResponse response1 = chatOrderResponses.get(0); // 이 메시지가 가장 최신 메시지, 즉 시간이 제일 늦다.
        ChatOrderResponse response2 = chatOrderResponses.get(1);
        ChatOrderResponse response3 = chatOrderResponses.get(2); // 이 메시지가 가장 이전에 작성된 메시지. 즉 시간이 제일 빠르다.

        assertThat(response1.getTime().isAfter(response2.getTime())).isTrue();
        assertThat(response2.getTime().isAfter(response3.getTime())).isTrue();
        assertThat(response1.getTime().isAfter(response3.getTime())).isTrue();
    }

    /**
     * save
     */
    @Test
    @DisplayName("case 1: 사용자의 MessageRequest가 정상적으로 저장되는 경우")
    void save() {
        // given
        String testNickname = TEST_USER_NICKNAME;
        String testMsg = "test hello";
        String testTime = "2023-08-04 23:05:24";
        MessageRequest messageRequest = MessageRequest.builder()
                .nickname(testNickname)
                .content(testMsg)
                .time(testTime)
                .build();

        // when
        chatService.save(messageRequest);

        // then
        List<Chat> chats = chatRepository.findAll();
        assertThat(chats.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("case 2: MessageRequest에서 time 형식이 잘못된 경우")
    void save_fail_when_time_format_is_wrong() {
        // given
        String testNickname = TEST_USER_NICKNAME;
        String testMsg = "test hello";
        String testTime = "2023-08-04T23:05:24";
        String testTime2 = "2023-08-04  23:05:24";
        String testTime3 = "2023-08-0423:05:24";
        MessageRequest messageRequest1 = MessageRequest.builder()
                .nickname(testNickname)
                .content(testMsg)
                .time(testTime)
                .build();
        MessageRequest messageRequest2 = MessageRequest.builder()
                .nickname(testNickname)
                .content(testMsg)
                .time(testTime2)
                .build();
        MessageRequest messageRequest3 = MessageRequest.builder()
                .nickname(testNickname)
                .content(testMsg)
                .time(testTime3)
                .build();

        // then
        assertThrows(CustomException.class, () -> chatService.save(messageRequest1));
        assertThrows(CustomException.class, () -> chatService.save(messageRequest2));
        assertThrows(CustomException.class, () -> chatService.save(messageRequest3));
    }

    /**
     * clear
     */
    @Test
    @DisplayName("db에 저장된 대화 내용이 정상적으로 삭제되는 경우, 결과물로 ChatResult 반환")
    void clear() {
        // given
        String testNickname = TEST_USER_NICKNAME;
        LocalDateTime now = LocalDateTime.now();
        List<Chat> chatList = Arrays.asList(
                Chat.builder()
                        .nickname(testNickname)
                        .role(ROLE_ASSISTANT)
                        .message("test message 1")
                        .time(now)
                        .build(),
                Chat.builder()
                        .nickname(testNickname)
                        .role(ROLE_USER)
                        .message("Here is test message 2")
                        .time(now.plusMinutes(10))
                        .build()
        );
        chatRepository.saveAll(chatList);

        // when
        // delete All data, and save one welcome message data
        ChatResult chatResult = chatService.clear(testNickname);

        // then
        List<Chat> chats = chatRepository.findAll();
        assertThat(chats.size()).isOne();

        assertThat(chatResult.getChatResponse()).isNotNull();   // order doesn't exists
        assertThat(chatResult.getChatOrderResponse()).isNull(); // order exists
    }

}