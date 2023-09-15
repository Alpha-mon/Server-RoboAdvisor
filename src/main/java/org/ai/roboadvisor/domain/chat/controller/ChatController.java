package org.ai.roboadvisor.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.chat.dto.request.ClearRequest;
import org.ai.roboadvisor.domain.chat.dto.request.MessageRequest;
import org.ai.roboadvisor.domain.chat.dto.response.ChatListResponse;
import org.ai.roboadvisor.domain.chat.dto.response.ChatResponse;
import org.ai.roboadvisor.domain.chat.service.ChatService;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "chat-bot", description = "ChatGPT를 이용한 챗봇 API")
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    private final int SUCCESS = 0;
    private final int TIME_INPUT_INVALID = -1;
    private final int INTERNAL_SERVER_ERROR = -100;

    @Operation(summary = "채팅 서비스로 입장하는 경우", description = """
            사용자가 채팅 서비스 입장 시, 기존의 대화 내용을 불러온다.
                        
            기존에 대화 내용이 존재하지 않는다면, 초기 메시지(Welcome Message)를 보내준다.

            order : 초기 메시지의 경우 null이며, 대화 내용을 불러오는 경우에는 숫자가 클수록 최근에 한 대화이다.

            role : user의 경우 사용자가 입력한 대화, role : assistant의 경우 ChatGPT가 응답하는 대화이다.
            """)
    @ApiResponse(responseCode = "200", description = "사용자가 채팅방에 처음 입장한 경우, 챗봇의 Welcome Message를 보낸다.",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "처음 입장한 경우 응답 예시",
                            value = """
                                        {
                                            "code": 200,
                                            "message": "기존 대화 내용을 정상적으로 불러왔습니다",
                                            "data": [
                                                {
                                                    "order": 3,
                                                    "role": "assistant",
                                                    "content": "선물거래는 미래에 주식이나 다른 자산을 특정 가격으로 사고 팔 수 있는 계약을 말합니다. 선물거래의 목적은 주로 투자자나 기업이 향후 시장 변동에 대비하기 위해 가격을 고정하고 리스크를 완화하는 것입니다. 예를 들어, 주식의 가격이 상승할 것으로 예상되는데 실제로 상승하지 않고 하락한다면, 선물거래 계약으로 인해 미리 고정한 가격으로 주식을 사는 상황을 피할 수 있습니다. 이를 통해 가격 변동으로 인한 손실을 방지하고 이익을 창출할 수 있는 장점이 있습니다.",
                                                    "time": "2023-09-15 01:16:37"
                                                },
                                                {
                                                    "order": 2,
                                                    "role": "user",
                                                    "content": "주식에서 선물거래의 의미는 무엇이니?",
                                                    "time": "2023-09-15 01:16:26"
                                                },
                                                {
                                                    "order": 1,
                                                    "role": "assistant",
                                                    "content": "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                                    "time": "2023-09-15 01:00:15"
                                                }
                                            ]
                                        }
                                    """
                    )))
    @ApiResponse(responseCode = "201", description = "사용자가 채팅방에 처음 입장한 경우, 챗봇의 Welcome Message를 보낸다.",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "처음 입장한 경우 응답 예시",
                            value = """
                                        {
                                            "code": 201,
                                            "message": "초기 대화 내용이 정상적으로 응답되었습니다",
                                            "data": [
                                                {
                                                    "order": null,
                                                    "role": "assistant",
                                                    "content": "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                                    "time": "2023-09-15 01:28:14"
                                                }
                                            ]
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @GetMapping(value = "/{nickname}")
    public ResponseEntity<SuccessApiResponse<List<ChatListResponse>>> getAllChatOfUser(
            @PathVariable("nickname") String nickname) {
        List<ChatListResponse> chatListResponses = chatService.getAllChatOfUser(nickname);

        ResponseEntity<SuccessApiResponse<List<ChatListResponse>>> response;
        if (chatMessageIsMoreThanOne(chatListResponses)) {
            response = ResponseEntity.status(HttpStatus.OK)
                    .body(SuccessApiResponse.success(SuccessCode.LOAD_CHAT_SUCCESS, chatListResponses));
        } else {
            if (checkIfUserEnterTheRoomAtFirstTime(chatListResponses)) {
                // This case is when the user enters the room at the first time.
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body(SuccessApiResponse.success(SuccessCode.WELCOME_MESSAGE_CREATED_SUCCESS, chatListResponses));
            } else {
                response = ResponseEntity.status(HttpStatus.OK)
                        .body(SuccessApiResponse.success(SuccessCode.LOAD_CHAT_SUCCESS, chatListResponses));
            }
        }

        return response;
    }

    @Operation(summary = "채팅 메시지 전송", description = "클라이언트로부터 사용자의 메시지를 받아서, ChatGPT로 보내고, 응답 결과를 받아 클라이언트로 전달한다")
    @ApiResponse(responseCode = "201", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                        {
                                            "code": 201,
                                            "message": "사용자의 채팅 메시지가 정상적으로 처리되었습니다",
                                            "data": {
                                                "role": "assistant",
                                                "content": "주식에서 선물거래란, 미래의 특정 시점에서 정해진 가격에 일정한 양의 주식을 매수 또는 매도하는 계약을 말합니다. 이는 미래의 주가 움직임에 대한 예측이나 투자 전략을 기반으로 한 거래이며, 주가 상승이나 하락에 따른 이익을 추구하는 목적으로 사용될 수 있습니다. 선물거래는 주식 시장 외에도 다양한 자산에 대해 진행될 수 있으며, 투기적인 요소가 높은 거래 방식 중 하나입니다.",
                                                "time": "2023-09-15 01:28:51"
                                            }
                                        }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "시간 형식을 잘못 입력한 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "시간 형식을 잘못 입력한 경우 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "time 형식을 yyyy-MM-dd HH:mm:ss으로 작성해 주세요",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessApiResponse<ChatResponse>> sendChatBotMessage(@RequestBody MessageRequest messageRequest) {
        int saveResult = chatService.saveChat(messageRequest);

        if (saveResult == SUCCESS) {
            ChatResponse result = chatService.getMessageFromApi(messageRequest.getNickname(), messageRequest.getContent());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessApiResponse.success(SuccessCode.CHAT_CREATED_SUCCESS, result));
        } else if (saveResult == TIME_INPUT_INVALID) {
            throw new CustomException(ErrorCode.TIME_INPUT_INVALID);
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "대화 내용 삭제", description = """
            버튼을 클릭하면, 이전 대화 내용이 전부 삭제된다.

            삭제됨과 동시에, data에 초기 메시지(Welcome Message)를 리스트 형식으로 담아서 다시 보내준다.""")
    @ApiResponse(responseCode = "201", description = "기존에 대화 내용을 모두 삭제한 뒤, 다시 초기 응답을 보내준다",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example", description = "정상 응답 예시",
                            value = """
                                        {
                                            "code": 201,
                                            "message": "기존 대화 내용이 정상적으로 삭제되었습니다",
                                            "data": [
                                                {
                                                    "order": null,
                                                    "role": "assistant",
                                                    "content": "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                                    "time": "2023-09-15 00:43:36"
                                                }
                                            ]
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PostMapping(value = "/clear")
    public ResponseEntity<SuccessApiResponse<List<ChatListResponse>>> clear(@RequestBody ClearRequest clearRequest) {
        String userNickname = clearRequest.getNickname();
        boolean clearResult = chatService.clear(userNickname);

        if (clearResult) {
            ChatListResponse response = chatService.createAndSaveWelcomeMessage(userNickname);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessApiResponse.success(SuccessCode.CHAT_DELETED_SUCCESS, Collections.singletonList(response)));
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    protected boolean chatMessageIsMoreThanOne(List<ChatListResponse> messages) {
        int MESSAGE_IS_MORE_THAN_ONE = 1;
        return messages.size() > MESSAGE_IS_MORE_THAN_ONE;
    }

    protected boolean checkIfUserEnterTheRoomAtFirstTime(List<ChatListResponse> messages) {
        return messages.get(0).getOrder() == null;
    }
}
