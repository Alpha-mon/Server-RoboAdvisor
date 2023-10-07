package org.ai.roboadvisor.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private Long postId;    // 게시글 번호
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public static CommentResponse of(Long id, Long postId, String nickname, String content, LocalDateTime createdDateTime) {
        return new CommentResponse(id, postId, nickname, content, createdDateTime);
    }

    public static CommentResponse fromCommentEntity(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getPost().getId(),
                comment.getNickname(), comment.getContent(), comment.getCreatedDateTime());
    }

}
