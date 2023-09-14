package org.ai.roboadvisor.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.chat.entity.Chat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Schema(description = "클라이언트의 요청을 보낼 때 사용하는 JSON 요청 예시")
public class MessageRequest {

    @Schema(description = "사용자 정보: 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "사용자의 챗봇 입력 메세지", example = "주식에서 선물거래의 의미는 무엇이니?")
    @NotBlank
    private String content;

    @Schema(description = "메시지 입력 시간", example = "2023-08-04 04:20:19")
    @NotBlank
    private String time;

    @Builder
    private MessageRequest(String nickname, String content, String time) {
        this.nickname = nickname;
        this.content = content;
        this.time = time;
    }

    public static Chat toChatEntity(MessageRequest messageRequest, LocalDateTime time) {
        return Chat.builder()
                .nickname(messageRequest.getNickname())
                .role("user")
                .message(messageRequest.getContent())
                .time(time)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRequest that = (MessageRequest) o;
        return Objects.equals(nickname, that.nickname)
                && Objects.equals(content, that.content)
                && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, content, time);
    }
}
