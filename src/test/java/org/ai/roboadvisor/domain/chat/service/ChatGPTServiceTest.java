package org.ai.roboadvisor.domain.chat.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.gson.Gson;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.dto.request.ChatGptRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatGptResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@WireMockTest
@ActiveProfiles("test")
class ChatGPTServiceTest {

    @Autowired
    private ChatService chatService;

    private WebClient webClient;
    private static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
    private String gptJsonResponse;

    @Value("${openai.model}")
    private String OPEN_AI_MODEL;

    @BeforeEach
    public void setUp() {
        wireMockServer.start();
        String baseUrl = String.format("http://localhost:%s", wireMockServer.port());
        this.webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        gptJsonResponse = "{\n" +
                "  \"id\": \"chatcmpl-123\",\n" +
                "  \"object\": \"chat.completion\",\n" +
                "  \"created\": 1677652288,\n" +
                "  \"choices\": [{\n" +
                "    \"index\": 0,\n" +
                "    \"message\": {\n" +
                "      \"role\": \"assistant\",\n" +
                "      \"content\": \"주식에서 선물이란 ... 입니다.\"\n" +
                "    },\n" +
                "    \"finish_reason\": \"stop\"\n" +
                "  }],\n" +
                "  \"usage\": {\n" +
                "    \"prompt_tokens\": 9,\n" +
                "    \"completion_tokens\": 12,\n" +
                "    \"total_tokens\": 21\n" +
                "  }\n" +
                "}";
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    /**
     * getMessageFromApi(String userEmail, String message)
     */
    @Test
    @DisplayName("정상적으로 요청이 수행되는 경우")
    void getMessageFromApi() {
        // given
        String testMsg = "주식에서 선물 거래의 의미는 뭐니?";
        ChatGptRequest gptRequest = ChatGptRequest
                .builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(Message.builder()
                        .role("user")
                        .content(testMsg)
                        .build()))
                .build();
        Gson gson = new Gson();
        String gptRequestStr = gson.toJson(gptRequest);

        // when
        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/"))
                .withRequestBody(equalToJson(gptRequestStr))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(gptJsonResponse)));

        // then
        ChatGPTService chatGptService = new ChatGPTService(webClient);
        ChatGptResponse chatGptResponse = chatGptService.getMessageFromGPT(gptRequest).block();

        assertThat(chatGptResponse).isNotNull();
        String responseRole = chatGptResponse.getChoices().get(0).getMessage().getRole();
        String responseMessage = chatGptResponse.getChoices().get(0).getMessage().getContent();
        assertThat(responseRole).isEqualTo("assistant");
        assertThat(responseMessage).isNotBlank();
    }

    @Test
    @DisplayName("ChatGPT API를 통해 받은 데이터가 null이면, CustomException 리턴")
    void getMessageFromGpt_should_throw_CustomException_when_result_is_null() {
        // given
        String testMsg = "주식에서 선물 거래의 의미는 뭐니?";
        ChatGptRequest gptRequest = ChatGptRequest
                .builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(Message.builder()
                        .role("user")
                        .content(testMsg)
                        .build()))
                .build();

        Gson gson = new Gson();
        String gptRequestStr = gson.toJson(gptRequest);

        // here is different
        gptJsonResponse = null;

        // when
        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/"))
                        .withRequestBody(equalToJson(gptRequestStr))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)
                                .withBody(gptJsonResponse)));

        // then
        ChatGPTService chatGptService = new ChatGPTService(webClient);
        //assertThrows(CustomException.class, () -> chat.getMessageFromApi(gptRequest));
    }

    /**
     * getMessageFromGPT(ChatGptRequest gptRequest)
     */
    @Test
    @DisplayName("ChatGPT API 테스트")
    void getMessageFromGPT() {
        // given
        ChatGptRequest gptRequest = ChatGptRequest
                .builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(Message.builder()
                        .role("user")
                        .content("주식에서 선물 거래의 의미가 뭐니?")
                        .build()))
                .build();
        Gson gson = new Gson();
        String gptRequestStr = gson.toJson(gptRequest);

        // when
        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/"))
                        .withRequestBody(equalToJson(gptRequestStr))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)
                                .withBody(gptJsonResponse)));

        // then
        ChatGPTService chatGptService = new ChatGPTService(webClient);
        ChatGptResponse responseMono = chatGptService.getMessageFromGPT(gptRequest).block();

        assertThat(responseMono).isNotNull();
        assertThat(responseMono.getId()).isEqualTo("chatcmpl-123");
        assertThat(responseMono.getChoices().get(0).getMessage().getContent())
                .isEqualTo("주식에서 선물이란 ... 입니다.");
        assertThat(responseMono.getUsage().getTotalTokens()).isEqualTo(21);
    }
}