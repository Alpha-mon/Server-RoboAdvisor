package org.ai.roboadvisor.domain.community.swagger_annotation.comment.save;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "201", description = """
        정상 응답. data로 댓글 정보를 리턴한다.
                    
        id: 댓글 고유 번호(식별 번호), postId: 댓글이 작성된 게시글의 번호, nickname: 댓글 작성자 닉네임,
                    
        content: 댓글 작성 내용, createdDateTime: 댓글이 작성된 시간
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                   {
                                       "code": 201,
                                       "message": "댓글이 정상적으로 등록되었습니다",
                                       "data": {
                                           "id": 2,
                                           "postId": 1,
                                           "nickname": "testUser",
                                           "content": "안녕하세요 댓글 1",
                                           "createdDateTime": "2023-09-25 03:14:21"
                                       }
                                   }
                                """
                )))
public @interface save_CREATED {
}

