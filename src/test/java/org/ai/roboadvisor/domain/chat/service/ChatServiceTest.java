package org.ai.roboadvisor.domain.chat.service;

import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResponse;
import org.ai.roboadvisor.domain.chat.entity.Chat;
import org.ai.roboadvisor.domain.chat.repository.ChatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


//@Transactional // MongoDB does not support @Transactional
@SpringBootTest
@ActiveProfiles("test")
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

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
     * getChatList(String email)
     */
    @Test
    @DisplayName("db에 대화내용이 존재하지 않는 경우 Welcome Message를 리스트에 담아서 Controller로 전달한다")
    void getChatList_when_data_is_null() {
        // given
        String testEmail = "test@test.com";

        // when
        List<ChatResponse> list = chatService.getAllChatOfUser(testEmail);

        // then
        assertThat(list.isEmpty()).isFalse();   // check data is exists

        ChatResponse result = list.get(0);
        assertThat(result.getOrder()).isNull();
        assertThat(result.getRole()).isEqualTo(ROLE_ASSISTANT);
        assertThat(result.getContent()).isEqualTo(WELCOME_MESSAGE);
    }

    @Test
    @DisplayName("db에 대화내용이 존재하는 경우 기존 대화 내용을 List로 담아 Controller로 전달한다")
    void getChatList_when_data_already_exists_in_db() {
        // given
        String testEmail = "test@test.com";

        LocalDateTime now = LocalDateTime.now();
        List<Chat> chatList = Arrays.asList(
                Chat.builder()
                        .email(testEmail)
                        .role(ROLE_ASSISTANT)
                        .message(WELCOME_MESSAGE)
                        .time(now)
                        .build(),
                Chat.builder()
                        .email(testEmail)
                        .role(ROLE_USER)
                        .message("주식시장에서, 선물 거래의 의미는 무엇이니?")
                        .time(now.plusSeconds(3000))
                        .build(),
                Chat.builder()
                        .email(testEmail)
                        .role(ROLE_ASSISTANT)
                        .message("선물이란 .. 입니다.")
                        .time(now.plusSeconds(6000))
                        .build()
        );
        chatRepository.saveAll(chatList);

        // when
        List<ChatResponse> resultList = chatService.getAllChatOfUser(testEmail);

        // then
        assertThat(resultList.size()).isEqualTo(3);

        // Order 1, 2, 3이 정상적으로 리턴되는지 확인
        List<Integer> orderList = Arrays.asList(resultList.get(0).getOrder(), resultList.get(1).getOrder(),
                resultList.get(2).getOrder());
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
        ChatResponse response1 = resultList.get(0); // 이 메시지가 가장 최신 메시지, 즉 시간이 제일 늦다.
        ChatResponse response2 = resultList.get(1);
        ChatResponse response3 = resultList.get(2); // 이 메시지가 가장 이전에 작성된 메시지. 즉 시간이 제일 빠르다.

        // remove 'T'
        String time1 = response1.getTime().replace("T", " ");
        String time2 = response2.getTime().replace("T", " ");
        String time3 = response3.getTime().replace("T", " ");

        // String -> LocalDateTime to compare times
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse(time1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(time2, formatter);
        LocalDateTime dateTime3 = LocalDateTime.parse(time3, formatter);

        assertThat(dateTime1.isAfter(dateTime2)).isTrue();
        assertThat(dateTime1.isAfter(dateTime3)).isTrue();
        assertThat(dateTime2.isAfter(dateTime3)).isTrue();
    }

    /**
     * getMessageFromApi(String userEmail, String message)
     */
    /*@Test
    @DisplayName("")
    void getMessageFromApi() {
        // given
        String testEmail = "test@test.com";
        String testMsg = "오늘 삼성전자의 주가는 얼마니?";


        // when
        Message messageFromApi = chatService.getMessageFromApi(testEmail, testMsg);


        // then
        System.out.println("messageFromApi = " + messageFromApi.getRole());
        System.out.println("messageFromApi.getContent() = " + messageFromApi.getContent());

    }*/


    /**
     * saveChat()
     */
    @Test
    @DisplayName("사용자의 MessageRequest가 정상적으로 저장되는 경우 true 리턴")
    void saveChat() {
        // given
        String testEmail = "test_1@test.com";
        String testMsg = "test hello";
        String testTime = "2023-08-04 23:05:24";
        MessageRequest messageRequest = MessageRequest.builder()
                .email(testEmail)
                .content(testMsg)
                .time(testTime)
                .build();
        // when
        boolean result = chatService.saveChat(messageRequest);

        // then
        assertThat(result).isTrue();

        // double check if data exists
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(testEmail));
        List<Chat> resultList = mongoTemplate.find(query, Chat.class);
        Chat userChat = resultList.get(0);

        assertThat(userChat).isNotNull();
        assertThat(userChat.getEmail()).isEqualTo(testEmail);
        assertThat(userChat.getRole()).isEqualTo("user");
        assertThat(userChat.getMessage()).isEqualTo(testMsg);
    }

    /**
     * clear()
     */
    @Test
    @DisplayName("db에 저장된 대화 내용이 정상적으로 삭제되는 경우 true 리턴")
    void clear() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String email = "test@test.com";
        List<Chat> chatList = Arrays.asList(
                Chat.builder()
                        .email(email)
                        .role(ROLE_ASSISTANT)
                        .message("test message 1")
                        .time(now)
                        .build(),
                Chat.builder()
                        .email(email)
                        .role(ROLE_USER)
                        .message("Here is test message 2")
                        .time(now.plusMinutes(10))
                        .build()
        );
        chatRepository.saveAll(chatList);

        // when
        boolean result = chatService.clear(email);

        // then
        assertThat(result).isTrue();

        // double check if data is not exists
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        List<Chat> resultList = mongoTemplate.find(query, Chat.class);
        assertThat(resultList.isEmpty()).isTrue();
    }

    /**
     * createAndSaveWelcomeMessage()
     */
    @Test
    @DisplayName("정상적으로 요청이 처리되는 경우")
    void createAndSaveWelcomeMessage() {
        // given
        String testEmail = "test_2@kakao.com";

        // when
        ChatResponse response = chatService.createAndSaveWelcomeMessage(testEmail);

        // then
        assertThat(response.getRole()).isEqualTo(ROLE_ASSISTANT);
        assertThat(response.getOrder()).isNull();
        assertThat(response.getContent()).isEqualTo(WELCOME_MESSAGE);
    }

    @Test
    @DisplayName("정상적으로 요청이 처리되는 경우에 timezone 변경 처리가 정상적으로 되는지 검증")
    void createAndSaveWelcomeMessage_change_timezone_is_applied() throws InterruptedException {
        // given
        String testEmail = "test_2@kakao.com";
        LocalDateTime now = LocalDateTime.now().withNano(0);

        // when
        Thread.sleep(3000); // sleep 3 seconds
        ChatResponse response = chatService.createAndSaveWelcomeMessage(testEmail);

        // then
        String respTime = response.getTime();

        // parse String -> LocalDateTime
        respTime = respTime.replace("T", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(respTime, formatter);

        assertThat(now.isBefore(dateTime)).isTrue();
    }
}