package org.ai.roboadvisor.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private Tendency tendency;
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    private Long viewCount;

    public static PostResponse of(Long id, Tendency tendency, String nickname, String content, LocalDateTime createdDateTime,
                                  Long viewCount) {
        return new PostResponse(id, tendency, nickname, content, createdDateTime, viewCount);
    }

    public static PostResponse fromPostEntity(Post post) {
        return new PostResponse(post.getId(), post.getTendency(),
                post.getNickname(), post.getContent(), post.getCreatedDateTime(), post.getViewCount());
    }
}
