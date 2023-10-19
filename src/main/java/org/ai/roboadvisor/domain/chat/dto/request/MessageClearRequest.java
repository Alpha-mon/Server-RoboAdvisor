package org.ai.roboadvisor.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 대화 내역 삭제를 요청할 때 사용하는 JSON 요청 예시")
public class MessageClearRequest {

    @Schema(description = "사용자 정보: 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    public static MessageClearRequest of(String nickname) {
        return new MessageClearRequest(nickname);
    }
}
