package org.ai.roboadvisor.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.service.ChatService;
import org.ai.roboadvisor.global.common.dto.ApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chatbot")
public class ChatController {

    private final ChatService chatService;

    /* 채팅방 처음 입장 시, Welcome Message 전달 */
    public void sendWelcomeMessage() {

    }


    /* 사용자의 메시지를 받아서, GPT로 보내고, 결과를 Message DTO로 받아온다. */
    @PostMapping(value = "message", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Message>> sendChatBotMessage(@RequestBody String message) {
        Message gptResponse = chatService.getMessageFromGpt(message);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.MESSAGE_CREATED, gptResponse));
    }

}
