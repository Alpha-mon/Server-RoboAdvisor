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
        // check tendency type
        checkTendencyIsValid(tendency);

        Page<Post> posts = postRepository.findPostsByTendencyAndDeleteStatusIsFalse(tendency, pageable);
        return posts.stream()
                .map(BoardResponse::fromPostAndCommentCount)
                .collect(Collectors.toList());
    }

    private void checkTendencyIsValid(Tendency tendency) {
        if (tendency == Tendency.TYPE_NOT_EXISTS) {
            throw new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
        }
    }
}
