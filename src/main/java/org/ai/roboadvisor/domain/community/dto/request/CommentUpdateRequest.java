package org.ai.roboadvisor.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {

    @Schema(description = "댓글 번호", example = "1")
    private Long commentId;

    @Schema(description = "사용자의 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "댓글 내용", example = "안녕하세요. 댓글 작성 1입니다. ")
    @NotBlank
    private String content;
}
