package org.ai.roboadvisor.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.chat.entity.Chat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@Schema(description = "클라이언트의 요청을 보낼 때 사용하는 JSON 요청 예시")
public class MessageRequest {

    @Schema(description = "사용자 정보: 이메일", example = "test@google.com")
    @NotBlank
    private String email;

    @Schema(description = "사용자의 챗봇 입력 메세지", example = "주식에서 선물거래의 의미는 무엇이니?")
    @NotBlank
    private String message;

    @Schema(description = "메시지 입력 시간", example = "2023-08-04T04:20:19")
    @NotBlank
    private String time;

    @Builder
    private MessageRequest(String email, String message, String time) {
        this.email = email;
        this.message = message;
        this.time = time;
    }

    public static Chat toChat(MessageRequest messageRequest) {
        // Parse String time to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(messageRequest.getTime(), formatter);

        return Chat.builder()
                .email(messageRequest.getEmail())
                .role("user")
                .message(messageRequest.getMessage())
                .time(dateTime)
                .build();
    }

}
