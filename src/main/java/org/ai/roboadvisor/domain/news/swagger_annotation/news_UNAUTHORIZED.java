package org.ai.roboadvisor.domain.news.swagger_annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "401", description = """
                
        요청 권한이 없는 경우: 즉, 투자 성향 테스트를 진행하지 않아서 추천 주식 종목이 현재 존재하지 않는 경우
        """
        ,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "예시",
                        value = """
                                   {
                                       "code": 401,
                                       "message": "추천 주식 종목이 존재하지 않습니다. 투자 성향 테스트를 진행해주세요",
                                       "data": null
                                   }
                                """
                )))
public @interface news_UNAUTHORIZED {
}

