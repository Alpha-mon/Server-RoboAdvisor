package org.ai.roboadvisor.domain.tendency.swagger_annotation.updateTendency;

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
        사용자의 닉네임이 DB에 존재하지 않는 경우, 혹은 잘못된 투자 성향이 입력된 경우
                
        사용자의 닉네임이 DB에 존재하지 않는 경우 -> example1
                
        잘못된 투자 성향이 입력된 경우 -> example2
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = {@ExampleObject(name = "example1",
                        description = "사용자의 닉네임이 DB에 존재하지 않는 경우 예시",
                        value = """
                                   {
                                        "code": 400,
                                        "message": "가입된 사용자의 정보가 존재하지 않습니다",
                                        "data": null
                                    }
                                """
                ), @ExampleObject(name = "example2",
                        description = "잘못된 투자 성향이 입력된 경우 예시",
                        value = """
                                   {
                                        "code": 400,
                                        "message": "잘못된 투자 성향 형식이 입력되었습니다",
                                        "data": null
                                   }
                                """)}

        ))
public @interface updateTendency_BAD_REQUEST {
}
