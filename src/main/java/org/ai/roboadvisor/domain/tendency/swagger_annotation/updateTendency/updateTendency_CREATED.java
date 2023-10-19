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
@ApiResponse(responseCode = "201", description = """
        사용자가 입력한 투자 성향을 등록한다.
                    
        data 값으로 사용자 닉네임 및 등록된 투자 성향 결과를 반환한다.
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                    {
                                        "code": 201,
                                        "message": "투자 성향 테스트 결과가 정상적으로 등록되었습니다",
                                        "data": {
                                            "nickname": "testUser",
                                            "tendency": "SHEEP"
                                        }
                                    }
                                """
                )))
public @interface updateTendency_CREATED {
}
