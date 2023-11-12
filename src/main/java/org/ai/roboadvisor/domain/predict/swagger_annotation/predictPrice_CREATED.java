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
        1. 최종 예측 값(1개) -> "average_prediction” : 최종 예측 값
                
        2. 알고리즘 7개 값 ( 값 + 그래프 사용 ): Bollinger, MACD, MOK, RSI, STCK, VR, WR
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                   {
                                         "code": 201,
                                         "message": "데이터를 불러오는데 성공하였습니다",
                                         "data": {
                                             "average_prediction": 27.993946075439453,
                                             "Bollinger": 46.32568661925468,
                                             "MACD": 72.49999999999999,
                                             "MOK": 53.77427879303248,
                                             "RSI": 54.393491216080385,
                                             "STCK": 6144.5119058633145,
                                             "VR": 10.468841438124882,
                                             "WR": 48.06941693775633
                                         }
                                   }
                                """
                )))
public @interface predictPrice_CREATED {
}

