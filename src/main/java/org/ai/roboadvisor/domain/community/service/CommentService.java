package org.ai.roboadvisor.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.request.CommentDeleteRequest;
import org.ai.roboadvisor.domain.community.dto.request.CommentRequest;
import org.ai.roboadvisor.domain.community.dto.request.CommentUpdateRequest;
import org.ai.roboadvisor.domain.community.dto.response.CommentDeleteResponse;
import org.ai.roboadvisor.domain.community.dto.response.CommentResponse;
import org.ai.roboadvisor.domain.community.entity.Comment;
import org.ai.roboadvisor.domain.community.entity.DeleteStatus;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.community.repository.CommentRepository;
import org.ai.roboadvisor.domain.community.repository.PostRepository;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse save(Long postId, CommentRequest commentRequest) {
        // 사용자 정보를 가져와서, 게시글의 투자 성향과 일치하는지 확인 필요(댓글 작성 권한 확인)
        User user = validateUserAndGet(commentRequest.getNickname());
        Post existPost = validatePostAndGet(postId);
        checkUserHasAuthorization(user, existPost);

        // 부모 댓글 가져오기
        Comment parentComment = null;
        if (commentRequest.getParentCommentId() != null) {
            parentComment = getValidParentComment(commentRequest.getParentCommentId());
        }

        Comment newComment = createNewCommentFromRequest(commentRequest, existPost, parentComment);
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
    public CommentDeleteResponse delete(Long postId, CommentDeleteRequest commentDeleteRequest) {
        Comment existingComment = findExistingCommentById(postId, commentDeleteRequest.getCommentId());

        // validate if user has authority
        validateUserHasAuthority(commentDeleteRequest.getNickname(), existingComment);

        // manually delete a comment
        deleteComment(existingComment);

        return CommentDeleteResponse.fromCommentEntity(existingComment);
    }

    private User validateUserAndGet(String nickname) {
        return userRepository.findUserByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
    }

    private Post validatePostAndGet(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXISTED));
    }

    private Comment getValidParentComment(Long parentCommentId) {
        return commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_EXISTED));
    }

    private Comment createNewCommentFromRequest(CommentRequest commentRequest, Post existPost, Comment parentComment) {
        Comment newComment = CommentRequest.fromCommentRequest(commentRequest, existPost);
        if (parentComment != null) {
            newComment.setParent(parentComment);
            parentComment.getChildren().add(newComment);
        }
        return newComment;
    }

    private void checkUserHasAuthorization(User user, Post post) {
        // check tendency of post and comment is the same
        if (user.getTendency() != post.getTendency()) {
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
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXISTED));
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
