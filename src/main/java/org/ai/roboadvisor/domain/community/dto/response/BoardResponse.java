package org.ai.roboadvisor.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private Tendency tendency;
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    private Long viewCount;
    private int commentCount;   // number of comments in a post

    public static BoardResponse fromPostAndCommentCount(Post post) {
        return new BoardResponse(post.getId(), post.getTendency(), post.getNickname(),
                post.getContent(), post.getCreatedDateTime(), post.getViewCount(), post.getComments().size());
    }
}
