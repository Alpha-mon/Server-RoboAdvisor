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
                                       "message": "게시글 수정 혹은 삭제 권한이 존재하지 않습니다",
                                       "data": {
                                           "id": 1,
                                           "tendency": "LION",
                                           "nickname": "testUser",
                                           "content": "안녕하세요3333",
                                           "createdDateTime": "2023-09-21 01:06:20",
                                           "viewCount": 0
                                       }
                                   }
                                """
                )))
public @interface update_UNAUTHORIZED {
}

