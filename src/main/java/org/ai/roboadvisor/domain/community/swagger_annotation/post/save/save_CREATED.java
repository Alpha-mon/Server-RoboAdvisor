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
@ApiResponse(responseCode = "201", description = """
        정상 응답. data로 게시글 정보를 리턴한다.
                    
        id: 게시글 고유 번호(식별 번호), tendency: 투자 성향, nickname: 게시글 작성자 닉네임,
                    
        content: 게시글 작성 내용, time: 게시글 작성 시간, viewcount: 조회수
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                   {
                                        "code": 201,
                                        "message": "게시글이 정상적으로 등록되었습니다",
                                        "data": {
                                            "id": 12,
                                            "tendency": "LION",
                                            "nickname": "testUser",
                                            "content": "안녕하세요",
                                            "createdDateTime": "2023-09-25 04:47:32",
                                            "viewCount": 0
                                        }
                                    }
                                """
                )))
public @interface save_CREATED {
}

