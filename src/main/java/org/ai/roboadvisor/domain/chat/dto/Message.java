package org.ai.roboadvisor.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message {
    /**
     * ChatGptRequest, ChatGptResponse 에서 사용됨.
     */
    @Schema(description = "ChatGPT API 에서 제공하는 role 값. 사용자의 경우 'user', GPT 응답의 경우 'assistant' 로 저장된다")
    private String role;
    @Schema(description = "ChatGPT 의 답변 내용")
    private String content;

    @Schema(description = "응답 시간")
    private String time;

    @Builder
    private Message(String role, String content, String time) {
        this.role = role;
        this.content = content;
        this.time = time;
    }
}
