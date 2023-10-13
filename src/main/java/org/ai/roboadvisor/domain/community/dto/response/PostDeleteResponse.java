package org.ai.roboadvisor.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.entity.Post;

@Getter
@AllArgsConstructor
public class PostDeleteResponse {
    private Long id;

    public static PostDeleteResponse fromPostEntity(Post post) {
        return new PostDeleteResponse(post.getId());
    }
}
