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
    private String message;
    private String time;

    @Builder
    private ChatResponse(Integer order, String role, String message, String time) {
        this.order = order;
        this.role = role;
        this.message = message;
        this.time = time;
    }

    public static ChatResponse fromChat(Chat chat, Integer order) {
        return ChatResponse.builder()
                .order(order)
                .role(chat.getRole())
                .message(chat.getMessage())
                .time((chat.getTime().withNano(0)).toString())
                .build();
    }
}
