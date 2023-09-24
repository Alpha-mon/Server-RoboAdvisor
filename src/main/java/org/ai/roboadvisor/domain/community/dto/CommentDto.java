package org.ai.roboadvisor.domain.community.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.community.entity.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public static CommentDto fromComment(Comment comment) {
        return new CommentDto(comment.getId(), comment.getNickname(),
                comment.getContent(), comment.getCreatedDateTime());
    }
}
