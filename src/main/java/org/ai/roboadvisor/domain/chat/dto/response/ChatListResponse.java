package org.ai.roboadvisor.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.chat.entity.Chat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatListResponse {
    private Integer order;
    private String role;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    @Builder
    private ChatListResponse(Integer order, String role, String content, LocalDateTime time) {
        this.order = order;
        this.role = role;
        this.content = content;
        this.time = time;
    }

    public static ChatListResponse fromChatEntity(Chat chat, Integer order) {
        return ChatListResponse.builder()
                .order(order)
                .role(chat.getRole())
                .content(chat.getMessage())
                .time(chat.getTime())
                .build();
    }
}
