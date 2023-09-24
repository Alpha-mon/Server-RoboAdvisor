package org.ai.roboadvisor.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.request.PostDeleteRequest;
import org.ai.roboadvisor.domain.community.dto.request.PostRequest;
import org.ai.roboadvisor.domain.community.dto.response.PostResponse;
import org.ai.roboadvisor.domain.community.entity.DeleteStatus;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.community.repository.PostRepository;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.ai.roboadvisor.global.exception.ErrorIntValue.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse save(PostRequest postRequest) {
        checkTendencyIsValid(postRequest.getTendency());

        Long initViewCount = 0L;
        Post newPost = PostRequest.fromPostRequest(postRequest);
        newPost.setViewCount(initViewCount);    // set default value = 0

        // save
        savePost(newPost);

        return PostResponse.of(newPost.getId(), newPost.getTendency(), newPost.getNickname(),
                newPost.getContent(), newPost.getCreatedDateTime(), newPost.getViewCount());
    }

    @Transactional
    public PostResponse update(Long postId, PostRequest postRequest) {
        Post existingPost = findExistingPostById(postId);

        // check tendency type
        checkTendencyIsValid(postRequest.getTendency());

        // validate if user has authority
        validateUserHasAuthority(postRequest.getNickname(), existingPost);

        // update
        updatePostEntity(existingPost, postRequest);

        return PostResponse.fromPostEntity(existingPost);
    }

    @Transactional
    public int delete(Long postId, PostDeleteRequest postDeleteRequest) {
        Post existingPost = findExistingPostById(postId);

        // validate if user has authority
        validateUserHasAuthority(postDeleteRequest.getNickname(), existingPost);

        // manually delete a post
        deletePost(existingPost);
        return SUCCESS.getValue();
    }

    private void checkTendencyIsValid(Tendency tendency) {
        if (tendency == Tendency.TYPE_NOT_EXISTS) {
            throw new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
        }
    }

    private void savePost(Post post) {
        try {
            postRepository.save(post);
        } catch (Exception e) {
            log.error("Save error: ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Post findExistingPostById(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void validateUserHasAuthority(String requestNickname, Post existingPost) {
        if (!(requestNickname.equals(existingPost.getNickname()))) {
            throw new CustomException(ErrorCode.USER_HAS_NOT_AUTHORIZED);
        }
    }

    private void updatePostEntity(Post existingPost, PostRequest postRequest) {
        existingPost.setTendency(postRequest.getTendency());
        existingPost.setContent(postRequest.getContent());
    }

    private void deletePost(Post post) {
        post.setDeleteStatus(DeleteStatus.T);
    }
}
