package org.ai.roboadvisor.domain.chat.swagger_annotation.sendMessage;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "400", description = """
        1. 사용자의 닉네임이 DB에 존재하지 않는 경우 -> example1
                
        2. 시간 형식을 잘못 입력한 경우 -> example2
                
        e.g) 2023-03-03  11:11:11 (사이에 띄어쓰기가 두 칸인 경우), 2023-03-03T11:11:11 (사이에 문자가 끼워져 있는 경우), etc..
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = {@ExampleObject(name = "example1",
                        description = "사용자의 닉네임이 DB에 존재하지 않는 경우 예시",
                        value = """
                                   {
                                        "code": 400,
                                        "message": "가입된 사용자의 정보가 존재하지 않습니다",
                                        "data": null
                                    }
                                """
                ), @ExampleObject(name = "example2",
                        description = "시간 형식을 잘못 입력한 경우 예시",
                        value = """
                                   {
                                       "code": 400,
                                       "message": "time 형식을 yyyy-MM-dd HH:mm:ss으로 작성해 주세요",
                                       "data": null
                                   }
                                """
                )}
        ))
public @interface sendMessage_BAD_REQUEST {
}
