package org.ai.roboadvisor.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.user.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Schema(description = "이메일", example = "test@google.com")
    @Email
    private String email;

    @Schema(description = "비밀번호", example = "test12@@")
    @NotBlank
    private String password;

    @Schema(description = "닉네임", example = "test12")
    @NotBlank
    private String nickname;

    @Schema(description = "생년월일", example = "2000-03-24")
    @NotBlank
    private LocalDate birth;

    @Schema(description = "성별", example = "남")
    @NotBlank
    private String gender;

    @Schema(description = "직업", example = "대학생")
    @NotBlank
    private String career;

    public static User toUserEntity(SignUpRequest signUpRequest) {
        return User.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .birth(signUpRequest.getBirth())
                .gender(signUpRequest.getGender())
                .career(signUpRequest.getCareer())
                .build();
    }

}
