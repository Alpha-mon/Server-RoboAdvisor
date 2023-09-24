package org.ai.roboadvisor.domain.community.swagger_annotation.post.delete;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "200", description = """
        게시글이 정상적으로 삭제된 경우: 응답 객체는 '게시글 작성', '게시글 수정' 과 동일하다.
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                   {
                                       "code": 200,
                                       "message": "게시글 삭제가 정상적으로 처리되었습니다",
                                       "data": {
                                           "id": 12,
                                           "tendency": "LION",
                                           "nickname": "testUser",
                                           "content": "안녕하세요",
                                           "createdDateTime": "2023-09-25 04:47:33",
                                           "viewCount": 0
                                       }
                                   }
                                """
                )))
public @interface delete_OK {
}

