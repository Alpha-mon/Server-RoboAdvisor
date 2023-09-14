package org.ai.roboadvisor.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.dto.request.ChatGptRequest;
import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatGptResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatListResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatGPTService chatGPTService;
    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;
    private final String ROLE_USER = "user";
    private final String ROLE_ASSISTANT = "assistant";
    private final int KST_TO_UTC = 9;
    private final int UTC_TO_KST = -9;

    private final int SUCCESS = 0;
    private final int TIME_INPUT_INVALID = -1;
    private final int INTERNAL_SERVER_ERROR = -100;

    @Value("${openai.model}")
    private String OPEN_AI_MODEL;

    @Transactional
    public List<ChatListResponse> getAllChatOfUser(String nickname) {
        List<ChatListResponse> result = new ArrayList<>();

        if (!checkIfChatIsExistsInDb(nickname)) {
            ChatListResponse chatResponse = createAndSaveWelcomeMessage(nickname);
            result.add(chatResponse);
        } else {
            // 1. Sorting Data order by time and _id
            Sort sort = Sort.by(
                    Sort.Order.desc("time"),
                    Sort.Order.desc("_id") // Ignore milliseconds, so order _id if time is same
            );
            Query query = new Query().with(sort);
            query.addCriteria(Criteria.where("nickname").is(nickname));
            List<Chat> chatList = mongoTemplate.find(query, Chat.class);

            // 2. Create Chat Order
            for (int i = 0; i < chatList.size(); i++) {
                Chat thisChat = chatList.get(i);
                thisChat.setTimeZone(thisChat.getTime(), UTC_TO_KST); // UTC -> KST
                ChatListResponse dto = ChatListResponse.fromChatEntity(chatList.get(i), chatList.size() - i);
                result.add(dto);
            }
        }
        return result;
    }

    public int saveChat(MessageRequest messageRequest) {

        // MessageRequest time format 검증
        Optional<LocalDateTime> dateTimeOptional = parseDateTime(messageRequest.getTime());
        if (dateTimeOptional.isEmpty()) {
            return TIME_INPUT_INVALID; // or handle the error differently
        }

        Chat userChat = MessageRequest.toChatEntity(messageRequest, dateTimeOptional.get());
        try {
            userChat.setTimeZone(userChat.getTime(), KST_TO_UTC);   // KST -> UTC
            chatRepository.save(userChat);
            return SUCCESS;
        } catch (RuntimeException e) {
            return INTERNAL_SERVER_ERROR;
        }
    }

    public ChatResponse getMessageFromApi(String nickname, String message) {
        ChatGptRequest chatGptRequest = ChatGptRequest
                .builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(Message.builder()
                        .role(ROLE_USER)
                        .content(message)
                        .build()))
                .build();

        ChatGptResponse chatGptResponse = chatGPTService.getMessageFromGPT(chatGptRequest).block();

        if (chatGptResponse == null) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } else {
            String responseRole = chatGptResponse.getChoices().get(0).getMessage().getRole();
            String responseMessage = chatGptResponse.getChoices().get(0).getMessage().getContent();

            // 1. save message into DB
            LocalDateTime now = LocalDateTime.now().withNano(0);    // ignore milliseconds
            Chat chat = Chat.builder()
                    .nickname(nickname)
                    .role(responseRole)
                    .message(responseMessage)
                    .time(now)
                    .build();

            try {
                chat.setTimeZone(chat.getTime(), KST_TO_UTC);  // KST -> UTC
                chatRepository.save(chat);
            } catch (RuntimeException e) {
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 2. Return Message DTO to client
            return ChatResponse.builder()
                    .role(responseRole)
                    .content(responseMessage)
                    .time(now)
                    .build();
        }
    }

    public boolean clear(String email) {
        // Clear All data
        try {
            chatRepository.deleteAllByEmail(email);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public ChatListResponse createAndSaveWelcomeMessage(String nickname) {
        String WELCOME_MESSAGE = "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요";

        // 1. Create Chat Entity and Save
        LocalDateTime now = LocalDateTime.now().withNano(0);    // ignore milliseconds
        Chat chat = Chat.builder()
                .nickname(nickname)
                .role(ROLE_ASSISTANT)
                .message(WELCOME_MESSAGE)
                .time(now)
                .build();

        try {
            chat.setTimeZone(chat.getTime(), KST_TO_UTC);   // KST -> UTC
            chatRepository.save(chat);
            chat.setTimeZone(chat.getTime(), UTC_TO_KST); // UTC -> KST
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 2. Entity -> DTO and Return Welcome Message
        return ChatListResponse.fromChatEntity(chat, null);
    }

    private Optional<LocalDateTime> parseDateTime(String timeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return Optional.of(LocalDateTime.parse(timeString, formatter));
        } catch (DateTimeParseException e) {
            log.error(">> Failed to parse date-time string.", e);
            return Optional.empty();
        }
    }

    private boolean checkIfChatIsExistsInDb(String email) {
        return chatRepository.existsChatByNickname(email);
    }
}