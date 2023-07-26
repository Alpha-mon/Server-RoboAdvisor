package org.ai.roboadvisor.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@Schema(description = "클라이언트의 요청을 보낼 때 사용하는 JSON 요청 예시")
public class MessageRequest {
    /**
     * ChatController > sendChatBotMessage 의 @RequestBody 에 사용
     */
    @Schema(description = "사용자의 챗봇 입력 메세지", example = "주식에서 선물거래의 의미는 무엇이니?")
    @NotBlank
    private String message;
}
