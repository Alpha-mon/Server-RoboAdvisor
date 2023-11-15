package org.ai.roboadvisor.domain.predict.swagger_annotation;

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
        "negative_news_count” : 부정적 뉴스 수 (그래프에 사용)
                
                
        "positive_news_count”  : 긍정적 뉴스 수 (그래프에 사용)
                
                
        "negative_news_examples” : 부정적 뉴스 예시
                
                
        "positive_news_examples” : 긍정적 뉴스 예시
                
                
        "prediction": "negative” : 미래 주식 시장 → 긍정적/부정적이다. (메인)
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                   {
                                       "code": 201,
                                       "message": "데이터를 불러오는데 성공하였습니다",
                                       "data": {
                                           "negative_percentage": 1.5,
                                           "positive_percentage": 98.5,
                                           "prediction_result": "미래 주식 시장은 긍정적으로 예측됩니다 ."
                                       }
                                   }
                                """
                )))
public @interface predictMarket_CREATED {
}

