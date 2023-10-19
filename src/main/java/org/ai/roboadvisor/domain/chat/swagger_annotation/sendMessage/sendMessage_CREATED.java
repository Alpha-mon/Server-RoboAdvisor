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
                                                 "content": "주식에서 선물거래란 특정 기간 동안 미리 약정된 가격으로 주식을 매매하는 거래 방식을 의미합니다. 즉, 주식 선물거래는 약정된 가격으로 미래의 특정 시점에 주식을 사거나 팔 수 있는 계약을 말합니다. 이러한 선물거래는 투자자들이 주식 가격의 변동성으로부터 보호받을 수 있고, 투자 전략을 구성할 때 사용할 수 있는 도구로 이용될 수 있습니다. 주식 선물거래는 주식시장의 투기성과 리스크를 관리하고자 하는 투자자들에게 유용한 도구로 활용됩니다.",
                                                 "time": "2023-10-19 00:56:53"
                                             }
                                         }
                                     }
                                """
                )))
public @interface sendMessage_CREATED {
}
