package org.ai.roboadvisor.domain.chat.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.request.ChatGptRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatGptResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ChatGPTService {
    /**
     * Test Code 작성을 위해, ChatService에서 분리
     */

    private final WebClient chatGPTWebClient;

    public ChatGPTService(@Qualifier("chatGptWebClient") WebClient chatGPTWebClient) {
        this.chatGPTWebClient = chatGPTWebClient;
    }

    public Mono<ChatGptResponse> getMessageFromGPT(ChatGptRequest gptRequest) {
        Gson gson = new Gson();
        String jsonToStr = gson.toJson(gptRequest);

        return chatGPTWebClient.post()
                .body(BodyInserters.fromValue(jsonToStr))
                .retrieve()
                .bodyToMono(ChatGptResponse.class);
    }

}
