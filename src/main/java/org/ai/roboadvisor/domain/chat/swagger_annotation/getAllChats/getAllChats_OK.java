package org.ai.roboadvisor.domain.chat.swagger_annotation.getAllChats;

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
        case 2: 기존에 대화 내용이 존재하는 경우 - 기존에 존재하는 대화 메시지 전체를 불러온다.
                
        이 경우는 data 안에 chatOrderResponse 객체를 반환한다.
                        
        chatOrderResponse Array 객체 안에, order, role, content, time 정보가 각각 담긴다.
                
        role: assistant(ChatGPT 답변 내용), role: user(사용자가 입력한 대화)
                
        order 값이 높을 수록, 최근에 작성된 메시지이다. 따라서 order 값을 기준으로 정렬하면 되며, 혹은 time 값을 DateTime 타입으로 바꿔서 정렬도 가능하다.
                
        '현재는 Pagination을 따로 사용하지 않고, 전체 데이터를 모두 보내도록 구현되어 있음'
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "처음 입장한 경우 응답 예시",
                        value = """
                                    {
                                         "code": 200,
                                         "message": "기존 대화 내용을 정상적으로 불러왔습니다",
                                         "data": {
                                             "chatOrderResponse": [
                                                 {
                                                     "order": 3,
                                                     "role": "assistant",
                                                     "content": "선물거래는 주식 시장에서 특정 종목의 주식을 향후에 일정 가격으로 매매하는 거래 방식을 의미합니다. 주식을 선물로 거래하기 때문에 선물거래라고 불립니다. 예를 들어, 현재 시점에서 어떤 주식의 가격이 100원인데, 선물거래를 통해 이 주식을 3개월 후에 120원에 구매하거나 판매하는 것이 가능합니다. 선물거래는 주식을 장기적으로 보유하거나 투자자들이 가격 변동에 대한 위험을 헷지하기 위해 사용될 수 있습니다.",
                                                     "time": "2023-09-22 14:30:57"
                                                 },
                                                 {
                                                     "order": 2,
                                                     "role": "assistant",
                                                     "content": "안녕하세요, 저는 AI로보어드바이저의 ChatGPT 서비스에요! 궁금한 점을 입력해주세요",
                                                     "time": "2023-09-22 14:24:11"
                                                 },
                                                 {
                                                     "order": 1,
                                                     "role": "user",
                                                     "content": "주식에서 선물거래의 의미는 무엇이니?",
                                                     "time": "2023-09-22 01:28:20"
                                                 }
                                             ]
                                         }
                                     }
                                """
                )))
public @interface getAllChats_OK {
}
