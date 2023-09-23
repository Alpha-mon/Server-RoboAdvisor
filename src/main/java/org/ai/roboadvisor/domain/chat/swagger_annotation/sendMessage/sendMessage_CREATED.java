package org.ai.roboadvisor.domain.chat.swagger_annotation.sendMessage;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "201", description = "정상 응답",
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                    {
                                         "code": 201,
                                         "message": "사용자의 채팅 메시지가 정상적으로 처리되었습니다",
                                         "data": {
                                             "chatResponse": {
                                                 "role": "assistant",
                                                 "content": "주식에서 선물거래는 미래에 일정한 날짜에 주식을 사거나 팔 수 있는 계약을 말합니다. 이러한 계약은 특정 주식의 가격을 현재의 가격에서 미리 확정하고 미래에 거래가 이루어지도록 하는 것이 목적입니다. 주식 선물거래는 투자자들이 주식 시장의 가격 변동에 대해 원하는 방향으로 수익을 얻거나 손실을 최소화하기 위해 사용됩니다. 예를 들어, 주식 가격이 상승할 것으로 예상되는 투자자는 주식 선물계약을 매수하여 주식을 미리 구매하는 것으로서, 가격이 상승하면 이익을 얻을 수 있습니다. 반대로, 주식 가격이 하락할 것으로 예상되는 투자자는 주식 선물계약을 매도하여 주식을 미리 판매하는 것으로서, 가격이 하락하면 이익을 얻을 수 있습니다. 선물거래는 주식 가격 변동에 대한 투자자의 예측을 기반으로 이루어지므로, 주식 시장에서 높은 위험성을 가지고 있습니다.",
                                                 "time": "2023-09-23 13:24:10"
                                             }
                                         }
                                     }
                                """
                )))
public @interface sendMessage_CREATED {
}
