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
                                            "role": "assistant",
                                            "content": "주식에서 선물거래란, 미래의 특정 시점에서 정해진 가격에 일정한 양의 주식을 매수 또는 매도하는 계약을 말합니다. 이는 미래의 주가 움직임에 대한 예측이나 투자 전략을 기반으로 한 거래이며, 주가 상승이나 하락에 따른 이익을 추구하는 목적으로 사용될 수 있습니다. 선물거래는 주식 시장 외에도 다양한 자산에 대해 진행될 수 있으며, 투기적인 요소가 높은 거래 방식 중 하나입니다.",
                                            "time": "2023-09-15 01:28:51"
                                        }
                                    }
                                """
                )))
public @interface sendMessage_CREATED {
}
