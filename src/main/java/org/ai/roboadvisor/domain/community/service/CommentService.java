package org.ai.roboadvisor.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.request.CommentDeleteRequest;
import org.ai.roboadvisor.domain.community.dto.request.CommentRequest;
import org.ai.roboadvisor.domain.community.dto.request.CommentUpdateRequest;
import org.ai.roboadvisor.domain.community.dto.response.CommentResponse;
import org.ai.roboadvisor.domain.community.entity.Comment;
import org.ai.roboadvisor.domain.community.entity.DeleteStatus;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.community.repository.CommentRepository;
import org.ai.roboadvisor.domain.community.repository.PostRepository;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse save(Long postId, CommentRequest commentRequest) {
        Tendency commentTendency = commentRequest.getTendency();
        checkTendencyIsValid(commentTendency);

        Post existPost = postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_ID_NOT_EXISTS));
        checkCommentIsValid(commentTendency, existPost);

        Comment newComment = CommentRequest.fromCommentRequest(commentRequest, existPost);

        // save
        saveComment(newComment);
        return CommentResponse.fromCommentEntity(newComment);
    }

    @Transactional
    public CommentResponse update(Long postId, CommentUpdateRequest commentUpdateRequest) {
        Comment existingComment = findExistingCommentById(postId, commentUpdateRequest.getCommentId());

        // validate if user has authority
        validateUserHasAuthority(commentUpdateRequest.getNickname(), existingComment);

        // update
        updateCommentEntity(existingComment, commentUpdateRequest);

        return CommentResponse.fromCommentEntity(existingComment);
    }

    @Transactional
    public CommentResponse delete(Long postId, CommentDeleteRequest commentDeleteRequest) {
        Comment existingComment = findExistingCommentById(postId, commentDeleteRequest.getCommentId());

        // validate if user has authority
        validateUserHasAuthority(commentDeleteRequest.getNickname(), existingComment);

        // manually delete a comment
        deleteComment(existingComment);

        return CommentResponse.fromCommentEntity(existingComment);
    }

    private void checkTendencyIsValid(Tendency tendency) {
        if (tendency == Tendency.TYPE_NOT_EXISTS) {
            throw new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
        }
    }

    private void checkCommentIsValid(Tendency tendency, Post post) {
        // check tendency of post and comment is the same
        if (tendency != post.getTendency()) {
            throw new CustomException(ErrorCode.TENDENCY_NOT_MATCH_BETWEEN_POST_AND_COMMENT);
        }
    }

    private void saveComment(Comment comment) {
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            log.error("Save error: ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Comment findExistingCommentById(Long postId, Long commentId) {
        Post existPost = postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_ID_NOT_EXISTS));
        return commentRepository.findCommentByIdAndPost(commentId, existPost)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void validateUserHasAuthority(String requestNickname, Comment existingComment) {
        if (!(requestNickname.equals(existingComment.getNickname()))) {
            throw new CustomException(ErrorCode.USER_HAS_NOT_AUTHORIZED);
        }
    }

    private void updateCommentEntity(Comment existingComment, CommentUpdateRequest commentUpdateRequest) {
        existingComment.setContent(commentUpdateRequest.getContent());
    }

    private void deleteComment(Comment comment) {
        comment.setDeleteStatus(DeleteStatus.T);
    }

}
