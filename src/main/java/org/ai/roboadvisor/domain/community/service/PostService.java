package org.ai.roboadvisor.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.CommentDto;
import org.ai.roboadvisor.domain.community.dto.request.PostDeleteRequest;
import org.ai.roboadvisor.domain.community.dto.request.PostRequest;
import org.ai.roboadvisor.domain.community.dto.response.PostDeleteResponse;
import org.ai.roboadvisor.domain.community.dto.response.PostResponse;
import org.ai.roboadvisor.domain.community.entity.Comment;
import org.ai.roboadvisor.domain.community.entity.DeleteStatus;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.community.repository.CommentRepository;
import org.ai.roboadvisor.domain.community.repository.PostRepository;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse save(PostRequest postRequest) {
        User user = findExistingUserById(postRequest.getNickname());
        Tendency userTendency = user.getTendency();

        Long initViewCount = 0L;
        Post newPost = PostRequest.fromPostRequest(postRequest, userTendency);
        newPost.setViewCount(initViewCount);    // set default value = 0

        // save
        savePost(newPost);

        return PostResponse.fromPostEntity(newPost, new ArrayList<>()); // 생성시 댓글은 존재하지 않으므로, 초기 빈 리스트 세팅
    }

    @Transactional
    public PostResponse getPostById(Long id) {
        Post post = findExistingPostById(id);

        // update view
        post.setViewCount(post.getViewCount() + 1);

        return PostResponse.fromPostEntity(post, getCommentsAndConvertToCommentDtos(post.getComments()));
    }

    @Transactional
    public PostResponse update(Long postId, PostRequest postRequest) {
        User user = findExistingUserById(postRequest.getNickname());
        Post existingPost = findExistingPostById(postId);

        // validate if user has authority
        validateUserHasAuthority(user.getNickname(), existingPost);

        // update
        updatePostEntity(existingPost, postRequest);

        return PostResponse.fromPostEntity(existingPost, getCommentsAndConvertToCommentDtos(existingPost.getComments()));
    }

    @Transactional
    public PostDeleteResponse delete(Long postId, PostDeleteRequest postDeleteRequest) {
        Post existingPost = findExistingPostById(postId);

        // validate if user has authority
        validateUserHasAuthority(postDeleteRequest.getNickname(), existingPost);

        // manually delete a post
        deletePost(existingPost);

        // manually delete comments related to a post
        commentRepository.markCommentsAsDeleted(existingPost, DeleteStatus.T);
        for (Comment comment : existingPost.getComments()) {
            comment.setDeleteStatus(DeleteStatus.T);
        }

        return PostDeleteResponse.fromPostEntity(existingPost);
    }

    private List<CommentDto> getCommentsAndConvertToCommentDtos(List<Comment> comments) {
        // 연관관계를 맺은 엔티티간의 무한 참조를 방지하기 위해 DTO 객체를 사용
        return comments.stream()
                .filter(comment -> (comment.getDeleteStatus() == DeleteStatus.F))
                .map(CommentDto::fromComment)
                .collect(Collectors.toList());
    }

    private void savePost(Post post) {
        try {
            postRepository.save(post);
        } catch (Exception e) {
            log.error("Save error: ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private User findExistingUserById(String userNickname) {
        return userRepository.findUserByNickname(userNickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
    }

    private Post findExistingPostById(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXISTED));
    }

    private void validateUserHasAuthority(String requestNickname, Post existingPost) {
        if (!(requestNickname.equals(existingPost.getNickname()))) {
            throw new CustomException(ErrorCode.USER_HAS_NOT_AUTHORIZED);
        }
    }

    private void updatePostEntity(Post existingPost, PostRequest postRequest) {
        existingPost.setContent(postRequest.getContent());
        existingPost.setViewCount(existingPost.getViewCount() + 1); // add view count + 1
    }

    private void deletePost(Post post) {
        post.setDeleteStatus(DeleteStatus.T);
    }
}
