package org.ai.roboadvisor.domain.community.swagger_annotation.post.save;

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
        PostRequest 요청으로 입력된 사용자의 닉네임 정보가 db에 존재하지 않는 경우.
                
        즉, 사용자가 존재하지 않는 경우
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
public @interface save_BAD_REQUEST {
}

