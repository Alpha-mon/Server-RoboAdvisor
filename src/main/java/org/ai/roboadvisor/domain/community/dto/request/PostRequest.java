package org.ai.roboadvisor.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.community.entity.Post;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @Schema(description = "사용자의 투자 성향", example = "SHEEP")
    @NotBlank
    private String type;

    @Schema(description = "사용자의 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "게시글 내용", example = "안녕하세요. 게시글 1입니다. ")
    @NotBlank
    private String content;

    @Schema(description = "시간", example = "2023-09-18 02:44:33")
    @NotBlank
    private String time;

    public static Post fromPostRequest(PostRequest postRequest, LocalDateTime time) {
        return Post.builder()
                .type(postRequest.getType())
                .nickname(postRequest.getNickname())
                .content(postRequest.getContent())
                .time(time)
                .build();
    }
}
