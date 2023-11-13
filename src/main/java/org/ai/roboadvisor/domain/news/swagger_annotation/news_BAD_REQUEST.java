package org.ai.roboadvisor.domain.news.swagger_annotation;

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
        가입된 사용자의 닉네임이 존재하지 않는 경우
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "사용자가 존재하지 않는 경우 예시",
                        value = """
                                   {
                                       "code": 400,
                                       "message": "가입된 사용자의 정보가 존재하지 않습니다",
                                       "data": null
                                   }
                                """
                )))
public @interface news_BAD_REQUEST {
}

