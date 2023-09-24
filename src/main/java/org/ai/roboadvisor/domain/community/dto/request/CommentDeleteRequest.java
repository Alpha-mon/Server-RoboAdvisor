package org.ai.roboadvisor.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeleteRequest {

    @Schema(description = "댓글 번호", example = "1")
    private Long commentId;

    @Schema(description = "사용자의 닉네임", example = "testUser")
    @NotBlank
    private String nickname;
}
