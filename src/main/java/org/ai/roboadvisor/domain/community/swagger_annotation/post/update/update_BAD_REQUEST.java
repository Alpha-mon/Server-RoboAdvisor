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
@ApiResponse(responseCode = "400", description = """
        투자 성향이 LION(공격투자형), SNAKE(적극투자형), MONKEY(위험중립형), SHEEP(안정추구형) 외에 다른 값이 들어온 경우,
                
        즉, 존재하지 않는 투자 성향이 입력된 경우
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "투자 성향이 잘못 입력된 경우 예시",
                        value = """
                                   {
                                       "code": 400,
                                       "message": "잘못된 투자 성향 형식이 입력되었습니다",
                                       "data": null
                                   }
                                """
                )))
public @interface update_BAD_REQUEST {
}

