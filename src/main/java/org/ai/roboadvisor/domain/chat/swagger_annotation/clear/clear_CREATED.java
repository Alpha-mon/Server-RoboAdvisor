package org.ai.roboadvisor.domain.chat.swagger_annotation.clear;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
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
public @interface clear_CREATED {
}
