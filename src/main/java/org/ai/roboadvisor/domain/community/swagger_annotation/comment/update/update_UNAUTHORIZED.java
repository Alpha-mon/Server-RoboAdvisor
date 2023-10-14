package org.ai.roboadvisor.domain.community.swagger_annotation.comment.update;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "401", description = "댓글 수정 권한이 없는 경우, 즉 작성자와 수정을 요청한 사용자의 닉네임이 다른 경우",
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "댓글 수정 권한이 없는 경우",
                        value = """
                                   {
                                       "code": 401,
                                       "message": "사용자에게 권한이 존재하지 않습니다",
                                       "data": null
                                   }
                                """
                )
        ))
public @interface update_UNAUTHORIZED {
}

