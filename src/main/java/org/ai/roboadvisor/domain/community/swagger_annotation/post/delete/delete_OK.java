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
        게시글이 정상적으로 삭제된 경우: 
                
        "data" 값으로 삭제 요청된 게시글의 id값을 리턴한다.
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                   {
                                        "code": 200,
                                        "message": "게시글 삭제가 정상적으로 처리되었습니다",
                                        "data": {
                                            "id": 2
                                        }
                                    }
                                """
                )))
public @interface delete_OK {
}

