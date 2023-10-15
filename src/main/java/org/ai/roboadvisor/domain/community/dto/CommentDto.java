package org.ai.roboadvisor.domain.community.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {

    private Long commentId;
    private Long parentCommentId;
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public static CommentDto fromComment(Comment comment) {
        // 최상위 댓글일 경우, getParent() 값이 null이다.
        Long parentId = (comment.getParent() != null) ? comment.getParent().getId() : null;
        return new CommentDto(comment.getId(), parentId, comment.getNickname(),
                comment.getContent(), comment.getCreatedDateTime());
    }
}
