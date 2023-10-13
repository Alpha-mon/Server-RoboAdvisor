package org.ai.roboadvisor.domain.community.swagger_annotation.post.update;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "401", description = "게시글 수정 권한이 없는 경우",
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "게시글 수정 권한이 없는 경우 예시",
                        value = """
                                  {
                                        "code": 401,
                                        "message": "사용자에게 권한이 존재하지 않습니다",
                                        "data": null
                                    }
                                """
                )))
public @interface update_UNAUTHORIZED {
}

