package org.ai.roboadvisor.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;         // 댓글 id
    private Long parentCommentId;   // 부모 댓글 id
    private Long postId;            // 게시글 id
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public static CommentResponse of(Long commentId, Long parentCommentId, Long postId, String nickname, String content, LocalDateTime createdDateTime) {
        return new CommentResponse(commentId, parentCommentId, postId, nickname, content, createdDateTime);
    }

    public static CommentResponse fromCommentEntity(Comment comment) {
        // 최상위 댓글일 경우, getParent() 값이 null이다.
        Long parentId = (comment.getParent() != null) ? comment.getParent().getId() : null;
        return new CommentResponse(comment.getId(), parentId,
                comment.getPost().getId(), comment.getNickname(),
                comment.getContent(), comment.getCreatedDateTime());
    }

}
