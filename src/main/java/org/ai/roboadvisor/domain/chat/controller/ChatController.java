package org.ai.roboadvisor.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.Message;
import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.service.ChatService;
import org.ai.roboadvisor.global.annotation.ApiResponse_InternalServerError;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "chat-bot", description = "ChatGPT를 이용한 챗봇 서비스")
@RestController
@RequestMapping("/api/chatbot")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅 메시지 조회", description = "사용자가 채팅 서비스 입장 시, 기존의 대화 내용을 불러온다")
    @GetMapping
    public void getMessage() {

    }


    @Operation(summary = "채팅 메시지 전송", description = "사용자의 메시지를 받아서, ChatGPT로 보내고, 결과를 Message DTO로 전달한다")
    @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                        {
                                            "code" : 201,
                                            "message" : "요청이 정상적으로 생성되었습니다",
                                            "data" : {
                                                "role" : "assistant",
                                                "message" : "주식에서 선물이란 ...을 의미합니다."
                                            }
                                        }
                                    """
                    )))
    @ApiResponse_InternalServerError
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessApiResponse<Message>> sendChatBotMessage(@RequestBody MessageRequest messageRequest) {
        Message gptResponse = chatService.getMessageFromGpt(messageRequest.getMessage());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.MESSAGE_CREATED, gptResponse));
    }

}
