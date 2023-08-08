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

    @Operation(summary = "채팅 메시지 조회", description = "사용자가 채팅 서비스 입장 시, 기존의 대화 내용을 불러온다. " +
            "기존에 대화 내용이 존재하지 않는다면, 초기 메시지(Welcome Message)를 보내준다.\n\n" +
            "order : 초기 메시지의 경우 null이며, 대화 내용을 불러오는 경우 숫자가 클수록 최근에 한 대화이다.\n\n" +
            "role : user의 경우 사용자가 입력한 대화, role : assistant의 경우 ChatGPT가 응답하는 대화이다. ")
    @ApiResponse(responseCode = "200", description = "기존 대화 내용이 존재하는 경우, 불러온다",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "기존 대화 내용 불러오기 예시",
                            value = """
                                        {
                                           "code": 200,
                                           "message": "기존 대화 내용을 정상적으로 불러왔습니다",
                                           "data": [
                                             {
                                               "order": 2,
                                               "role": "assistant",
                                               "message": "국내 주식은 한국 내에서 운영되고 거래되는 주식을 말하며, 영향을 받는 경제, 법률, 규제 등은 대부분 한국에 관련된 것입니다. 국내 주식은 한국 증권거래소나 코스닥에 상장된 기업의 주식을 매매하며, 한국의 경제 동향과 내부 요인들에 따라 주가가 변동됩니다.\\n\\n반면에 해외 주식은 외국의 주식을 말하며, 국내외의 경제, 정치, 사회적 상황 등 다양한 요인에 영향을 받습니다. 해외 주식은 해당 국가의 증권거래소에 상장된 기업의 주식이나 외국에서 운영되는 거래소에서 거래될 수도 있습니다. 국내 주식보다는 해외 경제 동향이나 외부 요인에 영향을 받기 때문에 국내와는 별개의 주식 시장입니다.\\n\\n또한 해외 주식은 환율 변동에 따라 수익이나 손실이 발생할 수 있으며, 국내 주식보다 투자에 더 큰 위험이 따를 수 있습니다. 또한, 투자하기 위해서는 해당 국가의 법률, 규제, 세금 등을 이해하고 준수해야 하는 점도 주의해야 할 부분입니다.",
                                               "time": "2023-08-04T21:24:21"
                                             },
                                             {
                                               "order": 1,
                                               "role": "user",
                                               "message": "국내 해외 주식 차이가 뭐야?",
                                               "time": "2023-08-04T21:23:01"
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
                                              "message": "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                              "time": "2023-08-04T18:21:53"
                                            }
                                          ]
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @GetMapping(value = "/{userEmail}")
    public ResponseEntity<SuccessApiResponse<List<ChatResponse>>> getMessageList(
            @PathVariable("userEmail") String email) {
        List<ChatResponse> chatResponseList = chatService.getChatList(email);

        ResponseEntity<SuccessApiResponse<List<ChatResponse>>> response;
        if (chatResponseList.size() != 1) {
            response = ResponseEntity.status(HttpStatus.OK)
                    .body(SuccessApiResponse.success(SuccessCode.LOAD_CHAT_SUCCESS, chatResponseList));
        } else {
            if (chatResponseList.get(0).getOrder() == null) {
                // This case is when the user enters the room at the first time.
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body(SuccessApiResponse.success(SuccessCode.WELCOME_MESSAGE_CREATED_SUCCESS, chatResponseList));
            } else {
                response = ResponseEntity.status(HttpStatus.OK)
                        .body(SuccessApiResponse.success(SuccessCode.LOAD_CHAT_SUCCESS, chatResponseList));
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
                                            "code" : 201,
                                            "message" : "사용자의 채팅 메시지가 정상적으로 처리되었습니다",
                                            "data" : {
                                                "role" : "assistant",
                                                "message" : "주식에서 선물이란 ...을 의미합니다.",
                                                "time" : "2023-08-04 04:20:19"
                                            }
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessApiResponse<Message>> sendChatBotMessage(@RequestBody MessageRequest messageRequest) {
        boolean saveResult = chatService.saveChat(messageRequest);
        if (saveResult) {
            Message result = chatService.getMessageFromApi(messageRequest.getEmail(), messageRequest.getMessage());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessApiResponse.success(SuccessCode.CHAT_CREATED_SUCCESS, result));
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "대화 내용 삭제", description = "버튼을 클릭하면, 이전 대화 내용이 전부 삭제된다.\n\n" +
            "삭제됨과 동시에, data에 초기 메시지(Welcome Message)를 리스트 형식으로 담아서 다시 보내준다.")
    @ApiResponse(responseCode = "200", description = "이전 대화 내용을 초기화한 뒤, 다시 초기 응답을 보내준다",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example", description = "예시",
                            value = """
                                        {
                                          "code": 200,
                                          "message": "기존 대화 내용이 정상적으로 삭제되었습니다",
                                          "data": [
                                            {
                                              "order": null,
                                              "role": "assistant",
                                              "message": "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                              "time": "2023-08-04T22:11:08"
                                            }
                                          ]
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @GetMapping(value = "/clear/{userEmail}")
    public ResponseEntity<SuccessApiResponse<List<ChatResponse>>> clear(@PathVariable("userEmail") String email) {
        boolean clearResult = chatService.clear(email);

        if (clearResult) {
            ChatResponse response = chatService.createAndSaveWelcomeMessage(email);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(SuccessApiResponse.success(SuccessCode.CHAT_DELETED_SUCCESS, Collections.singletonList(response)));
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
