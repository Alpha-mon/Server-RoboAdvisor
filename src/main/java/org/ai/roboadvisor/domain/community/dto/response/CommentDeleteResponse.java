package org.ai.roboadvisor.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Comment;

@Getter
@AllArgsConstructor
public class CommentDeleteResponse {
    private Long id;

    public static CommentDeleteResponse fromCommentEntity(Comment comment) {
        return new CommentDeleteResponse(comment.getId());
    }
}
