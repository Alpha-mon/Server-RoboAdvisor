package org.ai.roboadvisor.domain.community.swagger_annotation.comment.delete;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "400", description = """
        1. 게시글 번호가 잘못 입력된 경우, 혹은 존재하지 않는 경우 -> example1
                
        2. 댓글 번호가 잘못 입력된 경우, 혹은 존재하지 않는 경우 -> example2
                
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = {
                        @ExampleObject(name = "example1",
                                description = "게시글 번호가 잘못 입력된 경우, 혹은 존재하지 않는 경우 예시",
                                value = """
                                           {
                                                "code": 400,
                                                "message": "요청하신 게시글 id가 존재하지 않습니다",
                                                "data": null
                                           }
                                        """
                        ), @ExampleObject(name = "example2",
                        description = "댓글 번호가 잘못 입력된 경우, 혹은 존재하지 않는 경우 예시",
                        value = """
                                   {
                                        "code": 400,
                                        "message": "요청하신 댓글 id가 존재하지 않습니다",
                                        "data": null
                                   }
                                """
                )}
        ))
public @interface delete_BAD_REQUEST {
}

