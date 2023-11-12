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
                                            "negative_news_count": 9.0,
                                            "positive_news_count": 95.0,
                                            "negative_news_examples": [
                                                "‘비트코인 ETF’ 코앞?…“美 금융당국 움직임에 주목해야”",
                                                "현대차그룹이 발굴한 자율주행의 미래 ‘건국대 AutoKU-R팀’",
                                                "'메이드 인 부산' 전기차 2년뒤 출격…르노코리아 '영광' 재현할까",
                                                "주행거리 vs 가격…입맛 따라 전기차 배터리 골라 탄다",
                                                "'식당 소주값 6000원 시대' 막고 나서는 정부와 주류업계"
                                            ],
                                            "positive_news_examples": [
                                                "“크립토 윈터, 드디어 끝났나” 비트코인 급등한 3가지 이유",
                                                "환자식? NO!…맞춤형 건강 식단, ‘메디푸드’를 아십니까",
                                                "K패션 일본 강타, ‘칸코쿠 스타일’ 스고이~",
                                                "“맨발로 지구를 느낀다”···‘어싱’의 건강학 [스페셜 리포트]",
                                                "소규모 정비사업 활발…유의점은?"
                                            ],
                                            "prediction": "positive"
                                        }
                                    }
                                """
                )))
public @interface predictMarket_CREATED {
}

