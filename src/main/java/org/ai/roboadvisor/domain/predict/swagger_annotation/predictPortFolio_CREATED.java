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
        "predicted_volatilities” : 각 주식의 예측 변동성 값
                
                
        "optimal_weights” : 각 주식에 대한 최적의 포트폴리오 비중
                
                
        "stocks” : 주식 종목
                
                
        "risk_contributions” : 기여도 (밑 사진처럼 나타내고 싶음 → 그래프가 가능하다면 원합니다.)
                
                
        "explanations” : 비중이 너무 낮을 때만 출력
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                  {
                                      "code": 201,
                                      "message": "데이터를 불러오는데 성공하였습니다",
                                      "data": {
                                          "stocks": [
                                              "AAPL",
                                              "NVDA"
                                          ],
                                          "predicted_volatilities": {
                                              "AAPL": 0.09302105009555817,
                                              "NVDA": 0.00521440664306283
                                          },
                                          "explanations": [
                                              "",
                                              ""
                                          ],
                                          "optimal_weights": [
                                              0.053094215730708674,
                                              0.9469057842692914
                                          ],
                                          "risk_contributions": {
                                              "AAPL": 0.5000672251622688,
                                              "NVDA": 0.49993277483773124
                                          }
                                      }
                                  }
                                """
                )))
public @interface predictPortFolio_CREATED {
}

