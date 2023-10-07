package org.ai.roboadvisor.domain.community.swagger_annotation.post.getPostById;

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
        특정 게시글을 조회하는 경우
                
        comments Array 객체의 인덱스마다 댓글 객체의 정보가 담긴다. 
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                  {
                                        "code": 200,
                                        "message": "게시글을 조회하는데 성공하셨습니다",
                                        "data": {
                                            "id": 1,
                                            "tendency": "LION",
                                            "nickname": "testUser",
                                            "content": "안녕하세요3333",
                                            "createdDateTime": "2023-09-21 01:06:20",
                                            "viewCount": 22,
                                            "comments": [
                                                {
                                                    "id": 2,
                                                    "nickname": "testUser",
                                                    "content": "안녕하세요 댓글 1",
                                                    "createdDateTime": "2023-09-25 04:28:55"
                                                }
                                            ]
                                        }
                                    }
                                """
                )))
public @interface getPostById_OK {
}

