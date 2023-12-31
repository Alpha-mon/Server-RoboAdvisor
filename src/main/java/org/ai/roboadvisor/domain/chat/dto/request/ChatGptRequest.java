package org.ai.roboadvisor.domain.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.chat.dto.Message;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatGptRequest {

    private String model;

    private List<Message> messages;

    @Builder
    private ChatGptRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }
}
