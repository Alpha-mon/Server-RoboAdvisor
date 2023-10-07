package org.ai.roboadvisor.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.response.BoardResponse;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.community.repository.PostRepository;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<BoardResponse> getAllPostsByType(Tendency tendency, Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByTendencyAndDeleteStatusIsFalse(tendency, pageable);

        return posts.stream()
                .map(p -> {
                    int commentCnt = findCommentsByPostId(p.getId());
                    return BoardResponse.fromPostAndCommentCount(p, commentCnt);
                })
                .collect(Collectors.toList());
    }

    public int findCommentsByPostId(Long postId) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_ID_NOT_EXISTS));
        return post.getComments().size();
    }

}
