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
@ApiResponse(responseCode = "201", description = "사용자가 채팅방에 처음 입장한 경우, 챗봇의 Welcome Message를 보낸다.",
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
                                               "content": "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                               "time": "2023-09-22 14:32:58"
                                           }
                                       }
                                   }
                                """
                )))
public @interface getAllChats_CREATED {
}
