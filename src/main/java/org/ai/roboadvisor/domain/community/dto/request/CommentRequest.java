package org.ai.roboadvisor.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.community.entity.Comment;
import org.ai.roboadvisor.domain.community.entity.Post;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @Schema(description = """
            부모 댓글의 id.
                 
            최상위 댓글의 경우 null값을 담아서 요청을 보낸다.
                 
            대댓글의 경우 부모 댓글의 id값을 담아서 요청을 보낸다.

            """, example = "1")
    private Long parentCommentId;

    @Schema(description = "댓글 작성자의 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "댓글 내용", example = "안녕하세요. 댓글 작성 1입니다.")
    @NotBlank
    private String content;

    public static Comment fromCommentRequest(CommentRequest commentRequest, Post post) {
        return Comment.builder()
                .post(post)
                .nickname(commentRequest.getNickname())
                .content(commentRequest.getContent())
                .build();
    }
}
