package org.ai.roboadvisor.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.chat.entity.Chat;

@Getter
@NoArgsConstructor
public class ChatResponse {

    private Integer order;
    private String role;
    private String content;
    private String time;

    @Builder
    private ChatResponse(Integer order, String role, String content, String time) {
        this.order = order;
        this.role = role;
        this.content = content;
        this.time = time;
    }

    public static ChatResponse fromChat(Chat chat, Integer order) {
        return ChatResponse.builder()
                .order(order)
                .role(chat.getRole())
                .content(chat.getMessage())
                .time((chat.getTime().withNano(0)).toString())
                .build();
    }
}
