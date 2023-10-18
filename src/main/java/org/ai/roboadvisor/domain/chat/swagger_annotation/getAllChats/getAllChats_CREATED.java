package org.ai.roboadvisor.domain.chat.swagger_annotation.getAllChats;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "201", description = """
        case 1: 처음 입장하는 경우 - 서버에서 챗봇 초기 메시지(Welcome Message)를 보내준다.
                
        이 경우는 data 안에 chatResponse 객체를 반환한다.
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "처음 입장한 경우 응답 예시",
                        value = """
                                   {
                                        "code": 201,
                                        "message": "채팅방 입장에 성공하셨습니다",
                                        "data": {
                                            "chatResponse": {
                                                "role": "assistant",
                                                "content": "안녕하세요, 저는 알파몬의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                                "time": "2023-10-19 00:53:00"
                                            }
                                        }
                                    }
                                """
                )))
public @interface getAllChats_CREATED {
}
