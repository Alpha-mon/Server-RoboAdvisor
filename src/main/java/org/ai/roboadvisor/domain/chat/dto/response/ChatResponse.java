package org.ai.roboadvisor.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.chat.entity.Chat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatResponse {

    private String role;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    @Builder
    private ChatResponse(String role, String content, LocalDateTime time) {
        this.role = role;
        this.content = content;
        this.time = time;
    }

    public static ChatResponse fromChatEntity(Chat chat) {
        return ChatResponse.builder()
                .role(chat.getRole())
                .content(chat.getMessage())
                .time(chat.getTime())
                .build();
    }
}
