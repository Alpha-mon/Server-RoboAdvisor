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
@ApiResponse(responseCode = "200", description = """
        게시글이 정상적으로 수정된 경우: 응답 객체는 '게시글 작성' 과 동일하다.
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                  {
                                       "code": 200,
                                       "message": "게시글 수정이 정상적으로 처리되었습니다",
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
public @interface update_OK {
}

