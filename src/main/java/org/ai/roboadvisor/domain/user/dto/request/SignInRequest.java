package org.ai.roboadvisor.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @Schema(description = "이메일", example = "test@google.com")
    @Email
    private String email;

    @Schema(description = "비밀번호", example = "test12@@")
    @NotBlank
    private String password;
}
