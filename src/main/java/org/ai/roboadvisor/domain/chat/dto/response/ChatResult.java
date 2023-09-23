package org.ai.roboadvisor.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatResult {

    @JsonInclude(JsonInclude.Include.NON_NULL) // This will ignore property if it's null
    private List<ChatOrderResponse> chatOrderResponse;

    @JsonInclude(JsonInclude.Include.NON_NULL) // This will ignore property if it's null
    private ChatResponse chatResponse;

    public ChatResult(List<ChatOrderResponse> chatOrderResponse) {
        this.chatOrderResponse = chatOrderResponse;
        this.chatResponse = null;
    }

    public ChatResult(ChatResponse chatResponse) {
        this.chatOrderResponse = null;
        this.chatResponse = chatResponse;
    }
}
