package org.ai.roboadvisor.domain.chat.service;

import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.entity.Chat;
import org.ai.roboadvisor.domain.chat.repository.ChatRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ChatServiceExceptionTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

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
     * saveChat(MessageRequest messageRequest)
     */
    @Test
    void saveChat_should_return_false_when_save_fails() {
        // given
        MessageRequest messageRequest = MessageRequest.builder()
                .email("test-user@google.com")
                .content("hello test")
                .time("2023-08-04 04:20:19")
                .build();

        // set up your mock to throw an exception when the method is called
        doThrow(new DataIntegrityViolationException("error")).when(chatRepository).save(any(Chat.class));

        // when
        boolean result = chatService.saveChat(messageRequest);

        // then
        assertThat(result).isFalse();
        verify(chatRepository, times(1)).save(any(Chat.class));
    }

    /**
     * clear(String email)
     */
    @Test
    void clear_should_return_false_when_email_is_invalid() {
        // given
        String testEmail = "test@test.com";

        // set up your mock to throw an exception when the method is called
        doThrow(new DataIntegrityViolationException("error")).when(chatRepository).deleteAllByEmail(testEmail);

        // when
        boolean result = chatService.clear(testEmail);

        // then
        assertThat(result).isFalse();
        verify(chatRepository, times(1)).deleteAllByEmail(testEmail);
    }

    /**
     * createAndSaveWelcomeMessage(String email)
     */
    @Test
    void createAndSaveWelcomeMessage_should_return_CustomException_when_save_fails() {
        // given
        String testEmail = "test@test.com";

        // set up your mock to throw an exception when the method is called
        doThrow(new DataIntegrityViolationException("error")).when(chatRepository).save(any(Chat.class));

        // when
        assertThrows(CustomException.class, () -> chatService.createAndSaveWelcomeMessage(testEmail));
    }
}