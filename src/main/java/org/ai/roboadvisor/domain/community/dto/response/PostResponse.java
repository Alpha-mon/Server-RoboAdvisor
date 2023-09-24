package org.ai.roboadvisor.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ai.roboadvisor.domain.community.dto.CommentDto;
import org.ai.roboadvisor.domain.community.entity.DeleteStatus;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<CommentDto> comments;

    public static PostResponse fromPostEntity(Post post) {
        // 연관관계를 맺은 엔티티간의 무한 참조를 방지하기 위해 DTO 객체를 사용
        List<CommentDto> commentDtos = post.getComments()
                .stream()
                .filter(comment -> (comment.getDeleteStatus() == DeleteStatus.F))
                .map(CommentDto::fromComment)
                .collect(Collectors.toList());

        return new PostResponse(post.getId(), post.getTendency(),
                post.getNickname(), post.getContent(), post.getCreatedDateTime(),
                post.getViewCount(), commentDtos);
    }
}
