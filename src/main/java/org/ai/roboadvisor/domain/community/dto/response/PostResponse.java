package org.ai.roboadvisor.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Post;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String type;
    private String nickname;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    public static PostResponse of(Long id, String type, String nickname, String content, LocalDateTime time) {
        return new PostResponse(id, type, nickname, content, time);
    }

    public static PostResponse fromPostEntity(Post post) {
        return new PostResponse(post.getId(), post.getType(), post.getNickname(), post.getContent(), post.getTime());
    }
}
