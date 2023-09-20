package org.ai.roboadvisor.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @Schema(description = "사용자의 투자 성향", example = "SHEEP")
    @NotBlank
    private Tendency tendency;

    @Schema(description = "사용자의 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "게시글 내용", example = "안녕하세요. 게시글 1입니다. ")
    @NotBlank
    private String content;

    public static Post fromPostRequest(PostRequest postRequest) {
        return Post.builder()
                .tendency(postRequest.getTendency())
                .nickname(postRequest.getNickname())
                .content(postRequest.getContent())
                .build();
    }
}
