package org.ai.roboadvisor.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.dto.request.ChatGptRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatGptResponse;
import org.ai.roboadvisor.domain.chat.entity.Chat;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final Gson gson;
    private final ObjectMapper objectMapper;

    @Value("${openai.model}")
    private String OPEN_AI_MODEL;

    @Value("${openai.api.url}")
    private String OPEN_AI_URL;

    @Value("${openai.api.secret-key}")
    private String OPEN_AI_SECRET_KEY;

    @Transactional
    public List<Chat> getChatList() {

        return null;
    }

    public Message getMessageFromGpt(String message) {
        ChatGptRequest chatGptRequest = ChatGptRequest
                .builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(Message.builder()
                        .role("user")
                        .content(message)
                        .build()))
                .build();

        String result = sendRequestToGpt(chatGptRequest).block();


        // 여기서 DB에 메시지 저장


        try {
            ChatGptResponse chatGptResponse = objectMapper.readValue(result, ChatGptResponse.class);

            return Message.builder()
                    .role(chatGptResponse.getChoices().get(0).getMessage().getRole())
                    .content(chatGptResponse.getChoices().get(0).getMessage().getContent())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("[Error in ChatService -> getMessageFromGpt] ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Mono<String> sendRequestToGpt(ChatGptRequest gptRequest) {
        String jsonToStr = gson.toJson(gptRequest);

        WebClient webClient = WebClient.builder()
                .baseUrl(OPEN_AI_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPEN_AI_SECRET_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .body(BodyInserters.fromValue(jsonToStr))
                .retrieve()
                .bodyToMono(String.class);  // String.class can be replaced with a class if you have a model for the response
    }

}