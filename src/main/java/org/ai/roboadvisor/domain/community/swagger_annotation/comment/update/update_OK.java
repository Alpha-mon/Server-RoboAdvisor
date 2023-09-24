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
@ApiResponse(responseCode = "200", description = """
        댓글이 정상적으로 수정된 경우: data 내에 응답 객체는 '댓글 작성' 과 동일하다.
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                  {
                                        "code": 200,
                                        "message": "댓글 수정이 정상적으로 처리되었습니다",
                                        "data": {
                                            "id": 1,
                                            "postId": 1,
                                            "nickname": "testUser",
                                            "content": "안녕하세요 댓글 13333",
                                            "createdDateTime": "2023-09-25 03:12:52"
                                        }
                                    }
                                """
                )))
public @interface update_OK {
}

