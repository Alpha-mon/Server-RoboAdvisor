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
public @interface sendMessage_BAD_REQUEST {
}
