//package org.ai.roboadvisor.domain.chat.service;
//
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.client.WireMock;
//import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
//import com.github.tomakehurst.wiremock.junit.WireMockRule;
//import com.github.tomakehurst.wiremock.junit5.WireMockTest;
//import com.google.gson.Gson;
//import org.ai.roboadvisor.domain.chat.dto.Message;
//import org.ai.roboadvisor.domain.chat.dto.request.ChatGptRequest;
//import org.ai.roboadvisor.domain.chat.dto.response.ChatGptResponse;
//import org.ai.roboadvisor.domain.chat.entity.Chat;
//import org.ai.roboadvisor.domain.chat.repository.ChatRepository;
//import org.ai.roboadvisor.global.exception.CustomException;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.List;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//@ActiveProfiles("test")
//class ChatGPTService2Test {
//
//    @Autowired
//    private ChatService chatService;
//
//    @Autowired
//    private ChatRepository chatRepository;
//
//    /*
//    // 아래 webClient 대신 이 부분을 사용하면, 실제로 chatGPT API로 요청을 보낸다(Mocking이 안됨)
//    @Autowired
//    private WebClient chatGptWebClient;
//     */
//
//    private WebClient webClient;
//
//    private String gptJsonResponse;
//
//    static WireMockServer wireMockServer = new WireMockServer(8080);
//
//    @Value("${openai.model}")
//    private String OPEN_AI_MODEL;
//
//    private final String ROLE_USER = "user";
//    private final String ROLE_ASSISTANT = "assistant";
//    private String gptJsonResponseContent;
//
//    @BeforeEach
//    public void setUp() {
//        wireMockServer.start();
//
//        this.webClient = WebClient.builder().baseUrl("http://localhost:8080/")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//
//        gptJsonResponseContent = "주식에서 선물이란 ... 입니다.";
//
//        gptJsonResponse = "{\n" +
//                "  \"id\": \"chatcmpl-123\",\n" +
//                "  \"object\": \"chat.completion\",\n" +
//                "  \"created\": 1677652288,\n" +
//                "  \"choices\": [{\n" +
//                "    \"index\": 0,\n" +
//                "    \"message\": {\n" +
//                "      \"role\": \"" + ROLE_ASSISTANT + "\",\n" +
//                "       \"content\": \"" + gptJsonResponseContent + "\"\n" +
//                "    },\n" +
//                "    \"finish_reason\": \"stop\"\n" +
//                "  }],\n" +
//                "  \"usage\": {\n" +
//                "    \"prompt_tokens\": 9,\n" +
//                "    \"completion_tokens\": 12,\n" +
//                "    \"total_tokens\": 21\n" +
//                "  }\n" +
//                "}";
//    }
//
//    @AfterEach
//    void afterEach() {
//        wireMockServer.resetAll();
//
//        // delete mongodb manually
//        chatRepository.deleteAll();
//    }
//
//    @AfterAll
//    static void tearDown() {
//        if (wireMockServer != null) {
//            wireMockServer.stop();
//        }
//    }
//
//    /**
//     * getMessageFromApi(String userEmail, String message)
//     */
//    @Test
//    @DisplayName("정상적으로 API 결과를 받아오는 경우")
//    void getMessageFromApi() {
//        // given
//        String testEmail = "test@test.com";
//        String testContent = "주식 투자를 잘 하는 법을 알려줘!";
//
//        String testMsg = "주식에서 선물 거래의 의미는 뭐니?";
//        ChatGptRequest gptRequest = ChatGptRequest
//                .builder()
//                .model(OPEN_AI_MODEL)
//                .messages(List.of(Message.builder()
//                        .role("user")
//                        .content(testMsg)
//                        .build()))
//                .build();
//        Gson gson = new Gson();
//        String gptRequestStr = gson.toJson(gptRequest);
//
//        // when
//        stubFor(WireMock.post(WireMock.urlEqualTo("/"))
//                .withRequestBody(equalToJson(gptRequestStr))
//                .willReturn(WireMock.aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(200)
//                        .withBody(gptJsonResponse)));
//
//        // then
////        Message message = chatService.getMessageFromApi(testEmail, testContent);
////
////        List<Chat> chats = chatRepository.findAll();
////        assertThat(chats.size()).isEqualTo(1);
////
////        assertThat(message.getRole()).isEqualTo(ROLE_ASSISTANT);
//
//
////        ChatGPTService chatGptService = new ChatGPTService(webClient);
////        ChatGptResponse chatGptResponse = chatGptService.getMessageFromGPT(gptRequest).block();
////
////        assertThat(chatGptResponse).isNotNull();
////        String responseRole = chatGptResponse.getChoices().get(0).getMessage().getRole();
////        String responseMessage = chatGptResponse.getChoices().get(0).getMessage().getContent();
////        assertThat(responseRole).isEqualTo(ROLE_ASSISTANT);
////        assertThat(responseMessage).isEqualTo(gptJsonResponseContent);
//
//    }
//
//    @Test
//    @DisplayName("ChatGPT API를 통해 받은 데이터가 null이면, CustomException 리턴")
//    void getMessageFromGpt_should_throw_CustomException_when_result_is_null() {
//        // given
//        String testMsg = "주식에서 선물 거래의 의미는 뭐니?";
//        ChatGptRequest gptRequest = ChatGptRequest
//                .builder()
//                .model(OPEN_AI_MODEL)
//                .messages(List.of(Message.builder()
//                        .role("user")
//                        .content(testMsg)
//                        .build()))
//                .build();
//
//        Gson gson = new Gson();
//        String gptRequestStr = gson.toJson(gptRequest);
//
//        // here is different
//        gptJsonResponse = null;
//
//        // when
//        wireMockServer.stubFor(
//                WireMock.post(WireMock.urlEqualTo("/"))
//                        .withRequestBody(equalToJson(gptRequestStr))
//                        .willReturn(WireMock.aResponse()
//                                .withHeader("Content-Type", "application/json")
//                                .withStatus(200)
//                                .withBody(gptJsonResponse)));
//
//        // then
//        ChatGPTService chatGptService = new ChatGPTService(webClient);
//        //ChatGptResponse chatGptResponse = chatGptService.getMessageFromGPT(gptRequest).block();
//
////        System.out.println("chatGptResponse = " + chatGptResponse);
////        System.out.println("chatGptResponse = " + chatGptResponse);
//        Message msg = chatService.getMessageFromApi("t@t", "hi");
//        System.out.println("msg = " + msg);
//        System.out.println("msg.getTime() = " + msg.getTime());
//        System.out.println("msg.getRole() = " + msg.getRole());
//        System.out.println("msg.getContent() = " + msg.getContent());
//
//        //assertThrows(CustomException.class, () -> chatService.getMessageFromApi("t@t", "hi"));
//    }
//
//    /**
//     * getMessageFromGPT(ChatGptRequest gptRequest)
//     *//*
//    @Test
//    @DisplayName("ChatGPT API 테스트")
//    void getMessageFromGPT() {
//        // given
//        ChatGptRequest gptRequest = ChatGptRequest
//                .builder()
//                .model(OPEN_AI_MODEL)
//                .messages(List.of(Message.builder()
//                        .role("user")
//                        .content("주식에서 선물 거래의 의미가 뭐니?")
//                        .build()))
//                .build();
//        Gson gson = new Gson();
//        String gptRequestStr = gson.toJson(gptRequest);
//
//        // when
//        wireMockServer.stubFor(
//                WireMock.post(WireMock.urlEqualTo("/"))
//                        .withRequestBody(equalToJson(gptRequestStr))
//                        .willReturn(WireMock.aResponse()
//                                .withHeader("Content-Type", "application/json")
//                                .withStatus(200)
//                                .withBody(gptJsonResponse)));
//
//        // then
//        ChatGPTService chatGptService = new ChatGPTService(webClient);
//        ChatGptResponse responseMono = chatGptService.getMessageFromGPT(gptRequest).block();
//
//        assertThat(responseMono).isNotNull();
//        assertThat(responseMono.getId()).isEqualTo("chatcmpl-123");
//        assertThat(responseMono.getChoices().get(0).getMessage().getContent())
//                .isEqualTo("주식에서 선물이란 ... 입니다.");
//        assertThat(responseMono.getUsage().getTotalTokens()).isEqualTo(21);
//    }*/
//}