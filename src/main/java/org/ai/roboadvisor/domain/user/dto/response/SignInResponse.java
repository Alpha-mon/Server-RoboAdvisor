package org.ai.roboadvisor.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.user.entity.Tendency;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {

    @Schema(description = "사용자의 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "투자 성향", example = "SHEEP")
    private Tendency tendency;

    public static SignInResponse of(String nickname, Tendency tendency) {
        return new SignInResponse(nickname, tendency);
    }
}
