package org.ai.roboadvisor.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.dto.request.ChatGptRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatGptResponse;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

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

    public Message getMessageFromGpt(String message) {
        ChatGptRequest chatGptRequest = ChatGptRequest
                .builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(Message.builder()
                        .role("user")
                        .content(message)
                        .build()))
                .build();

        String response = sendRequestToGpt(chatGptRequest);

        try {
            ChatGptResponse chatGptResponse = objectMapper.readValue(response, ChatGptResponse.class);

            return Message.builder()
                    .role(chatGptResponse.getChoices().get(0).getMessage().getRole())
                    .content(chatGptResponse.getChoices().get(0).getMessage().getContent())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("[Error in ChatService -> getMessageFromGpt] ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public String sendRequestToGpt(ChatGptRequest gptRequest) {
        String jsonToStr = gson.toJson(gptRequest);
        String response;

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPEN_AI_URL))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Authorization", "Bearer " + OPEN_AI_SECRET_KEY)
                    .POST(BodyPublishers.ofString(jsonToStr))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            log.error("[Error in ChatService -> sendRequestToGpt] ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}