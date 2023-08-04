package org.ai.roboadvisor.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.dto.request.ChatGptRequest;
import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatGptResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResponse;
import org.ai.roboadvisor.domain.chat.entity.Chat;
import org.ai.roboadvisor.domain.chat.repository.ChatRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;
    private final String ROLE_USER = "user";
    private final String ROLE_ASSISTANT = "assistant";
    private final String WELCOME_MESSAGE = "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요";

    @Value("${openai.model}")
    private String OPEN_AI_MODEL;
    @Value("${openai.api.url}")
    private String OPEN_AI_URL;
    @Value("${openai.api.secret-key}")
    private String OPEN_AI_SECRET_KEY;

    @Transactional
    public List<ChatResponse> getChatList(String email) {
        List<ChatResponse> result = new ArrayList<>();

        boolean existsChatInDb = chatRepository.existsChatByEmail(email);
        if (!existsChatInDb) {
            ChatResponse chatResponse = createAndSaveWelcomeMessage(email);
            result.add(chatResponse);

        } else {
            // 1. Sorting Data order by time and _id
            Sort sort = Sort.by(
                    Sort.Order.desc("time"),
                    Sort.Order.desc("_id") // Ignore milliseconds, so order _id if time is same
            );
            Query query = new Query().with(sort);
            query.addCriteria(Criteria.where("email").is(email));
            List<Chat> chatList = mongoTemplate.find(query, Chat.class);

            // 2. Create Chat Order
            for (int i = 0; i < chatList.size(); i++) {
                ChatResponse dto = ChatResponse.fromChat(chatList.get(i), chatList.size() - i);
                result.add(dto);
            }
        }
        return result;
    }

    public boolean saveChat(MessageRequest messageRequest) {
        Chat userChat = MessageRequest.toChat(messageRequest);

        try {
            userChat.setTimeZone(userChat.getTime());
            chatRepository.save(userChat);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    public Message getMessageFromApi(String userEmail, String message) {
        ChatGptRequest chatGptRequest = ChatGptRequest
                .builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(Message.builder()
                        .role(ROLE_USER)
                        .content(message)
                        .build()))
                .build();

        String result = sendRequestToGpt(chatGptRequest).block();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ChatGptResponse chatGptResponse = objectMapper.readValue(result, ChatGptResponse.class);
            String responseRole = chatGptResponse.getChoices().get(0).getMessage().getRole();
            String responseMessage = chatGptResponse.getChoices().get(0).getMessage().getContent();

            // 1. save message into DB
            LocalDateTime now = LocalDateTime.now().withNano(0);    // ignore milliseconds
            Chat chat = Chat.builder()
                    .email(userEmail)
                    .role(responseRole)
                    .message(responseMessage)
                    .time(now)
                    .build();

            chat.setTimeZone(chat.getTime());
            chatRepository.save(chat);

            // 2. Return Message DTO to client
            return Message.builder()
                    .role(responseRole)
                    .content(responseMessage)
                    .time(now.toString())
                    .build();
        } catch (JsonProcessingException | RuntimeException e) {
            log.error("[Error in ChatService -> getMessageFromGpt] ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Mono<String> sendRequestToGpt(ChatGptRequest gptRequest) {
        Gson gson = new Gson();
        String jsonToStr = gson.toJson(gptRequest);

        WebClient webClient = WebClient.builder()
                .baseUrl(OPEN_AI_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPEN_AI_SECRET_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .body(BodyInserters.fromValue(jsonToStr))
                .retrieve()
                .bodyToMono(String.class);
    }

    public List<ChatResponse> clear(String email) {
        // 1. Clear All data
        try {
            chatRepository.deleteAllByEmail(email);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        ChatResponse chatResponse = createAndSaveWelcomeMessage(email);
        return List.of(chatResponse);
    }


    public ChatResponse createAndSaveWelcomeMessage(String email) {
        // 1. Create Chat Entity and Save
        LocalDateTime now = LocalDateTime.now().withNano(0);    // ignore milliseconds
        Chat chat = Chat.builder()
                .email(email)
                .role(ROLE_ASSISTANT)
                .message(WELCOME_MESSAGE)
                .time(now)
                .build();

        try {
            chat.setTimeZone(chat.getTime());   // UTC -> KST
            chatRepository.save(chat);
            chat.setTimeZone(chat.getTime().minusHours(18)); // rollback time
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 2. Entity -> DTO and Return Welcome Message
        return ChatResponse.fromChat(chat, null);
    }

}