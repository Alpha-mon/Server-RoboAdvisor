package org.ai.roboadvisor.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentUpdateResponse {

    private Long commentId;         // 댓글 id
    private Long postId;            // 게시글 id
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public static CommentUpdateResponse fromCommentEntity(Comment comment) {
        return new CommentUpdateResponse(comment.getId(),
                comment.getPost().getId(), comment.getNickname(),
                comment.getContent(), comment.getCreatedDateTime());
    }
}
