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
@Tag(name = "chat-bot", description = "ChatGPT를 이용한 챗봇 서비스")
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    private final int SUCCESS = 0;
    private final int TIME_INPUT_INVALID = -1;
    private final int INTERNAL_SERVER_ERROR = -100;

    @Operation(summary = "채팅 메시지 조회", description = """
            사용자가 채팅 서비스 입장 시, 기존의 대화 내용을 불러온다. 기존에 대화 내용이 존재하지 않는다면, 초기 메시지(Welcome Message)를 보내준다.

            order : 초기 메시지의 경우 null이며, 대화 내용을 불러오는 경우 숫자가 클수록 최근에 한 대화이다.

            role : user의 경우 사용자가 입력한 대화, role : assistant의 경우 ChatGPT가 응답하는 대화이다.""")
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
                                                    "time": "2023-09-15 00:43:36"
                                                }
                                            ]
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @GetMapping(value = "/{userEmail}")
    public ResponseEntity<SuccessApiResponse<List<ChatListResponse>>> getAllChatOfUser(
            @PathVariable("userEmail") String email) {
        List<ChatListResponse> chatListResponses = chatService.getAllChatOfUser(email);

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
                                                "content": "선물 거래란 어떤 자산(예: 주식, 원자재)에 대해 향후 일정한 날짜에 미리 약정된 가격으로 거래하는 것을 말합니다. 이는 향후 주가의 상승 또는 하락으로부터의 리스크를 회피하는 투자 방법 중 하나입니다. 선물 거래는 특히 변동성이 큰 주식 등의 투자에서 사용되며, 이를 통해 투자자는 예상치 못한 가격 변동에 대한 보호를 받을 수 있습니다.",
                                                "time": "2023-09-15 00:43:13"
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
            ChatResponse result = chatService.getMessageFromApi(messageRequest.getEmail(), messageRequest.getContent());
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
        String userEmail = clearRequest.getEmail();
        boolean clearResult = chatService.clear(userEmail);

        if (clearResult) {
            ChatListResponse response = chatService.createAndSaveWelcomeMessage(userEmail);
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
