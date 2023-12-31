package org.ai.roboadvisor.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.request.MessageClearRequest;
import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResult;
import org.ai.roboadvisor.domain.chat.service.ChatService;
import org.ai.roboadvisor.domain.chat.swagger_annotation.clear.clear_CREATED;
import org.ai.roboadvisor.domain.chat.swagger_annotation.getAllChats.getAllChats_CREATED;
import org.ai.roboadvisor.domain.chat.swagger_annotation.getAllChats.getAllChats_OK;
import org.ai.roboadvisor.domain.chat.swagger_annotation.sendMessage.sendMessage_BAD_REQUEST;
import org.ai.roboadvisor.domain.chat.swagger_annotation.sendMessage.sendMessage_CREATED;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "chat-bot", description = "ChatGPT를 이용한 챗봇 API")
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅 서비스 입장", description = """
            사용자가 채팅 서비스에 입장할 때 요청되는 함수
                        
            case 1: 처음 입장하는 경우 - 서버에서 챗봇 초기 메시지(Welcome Message)를 보내준다.
                        
            case 2: 기존에 대화 내용이 존재하는 경우 - 기존에 존재하는 대화 메시지 전체를 불러온다.

            order : 대화 내용을 불러오는 경우에 숫자가 클수록 최근에 한 대화이다.

            role : 'user' - 사용자가 입력한 대화, 'assistant' - ChatGPT가 응답하는 대화이다.
            """)
    @getAllChats_OK
    @getAllChats_CREATED
    @ApiResponse_Internal_Server_Error
    @GetMapping(value = "/{nickname}")
    public ResponseEntity<SuccessApiResponse<ChatResult>> enter(@PathVariable("nickname") String nickname) {
        ChatResult chatResult = chatService.enter(nickname);
        if (checkIfChatOrderResponseIsPresent(chatResult)) {
            // case LOAD_CHAT_SUCCESS
            return ResponseEntity.status(HttpStatus.OK)
                    .body(SuccessApiResponse.success(SuccessCode.LOAD_CHAT_SUCCESS, chatResult));
        } else {
            // case WELCOME_MESSAGE_CREATED_SUCCESS
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessApiResponse.success(SuccessCode.WELCOME_MESSAGE_CREATED_SUCCESS, chatResult));
        }
    }

    @Operation(summary = "채팅 메시지 전송", description = """
            사용자가 입력한 채팅 메시지를 전달한다.
                        
            프론트에서는 채팅 메시지를 서버로 보냄과 동시에 화면에 사용자가 보낸 채팅 메시지를 직접 띄우는 식으로 구현되어 있다. 
                
            서버에서는 사용자가 입력한 메시지를 ChatGPT API를 사용하여 전달한 다음, 응답 결과를 받아 반환한다.
                        
            Swagger 문서 하단의 Schemas 중 RequestBody로 'MessageRequest' 를 사용한다.
            """)
    @sendMessage_CREATED
    @sendMessage_BAD_REQUEST
    @ApiResponse_Internal_Server_Error
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessApiResponse<ChatResult>> sendMessage(@RequestBody MessageRequest messageRequest) {
        chatService.save(messageRequest);
        ChatResponse result = chatService.getMessageFromApi(messageRequest.getNickname(), messageRequest.getContent());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.CHAT_CREATED_SUCCESS, new ChatResult(result)));
    }

    @Operation(summary = "대화 내용 삭제", description = """
            기존 대화 내용이 전부 삭제된다.

            대화 내용이 삭제된 이후, data에 초기 메시지(Welcome Message)를 리스트 형식으로 담아서 다시 보내준다.
                        
            Swagger 문서 하단의 Schemas 중 RequestBody로 'MessageClearRequest' 를 사용한다.
            """)
    @clear_CREATED
    @ApiResponse_Internal_Server_Error
    @PostMapping(value = "/clear")
    public ResponseEntity<SuccessApiResponse<ChatResult>> clear(@RequestBody MessageClearRequest messageClearRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.CHAT_DELETED_SUCCESS,
                        chatService.clear(messageClearRequest)));
    }

    protected boolean checkIfChatOrderResponseIsPresent(ChatResult chatResult) {
        return chatResult.getChatOrderResponse() != null;
    }
}
