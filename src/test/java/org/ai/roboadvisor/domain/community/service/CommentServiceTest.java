package org.ai.roboadvisor.domain.community.service;

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
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.global.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private final Long POST_ID = 1L;
    private final String POST_NICKNAME = "post_user";
    private final Tendency POST_TENDENCY = Tendency.LION;
    private final String POST_CONTENT = "post_content";

    private final Long COMMENT_ID = 1L;
    private final String COMMENT_NICKNAME = "comment_user";
    private final Tendency COMMENT_TENDENCY = Tendency.LION;
    private final String COMMENT_CONTENT = "comment_content";

    @BeforeEach
    void setUp() {
        // 게시글 하나를 미리 저장
        Post post = Post.builder()
                .nickname(POST_NICKNAME)
                .tendency(POST_TENDENCY)
                .content(POST_CONTENT)
                .viewCount(0L)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        // 댓글 하나도 미리 저장
        Comment comment = Comment.builder()
                .nickname(COMMENT_NICKNAME)
                .content(COMMENT_CONTENT)
                .deleteStatus(DeleteStatus.F)
                .post(post)
                .build();
        post.getComments().add(comment);
        commentRepository.save(comment);
    }

    @Test
    @DisplayName("case 1: Tendency(투자 성향)이 적절하지 않은 타입이 온 경우, 예외처리")
    void save_with_TendencyIsNotValid() {
        Tendency notExists = Tendency.TYPE_NOT_EXISTS;
        CommentRequest request = new CommentRequest(
                notExists, "test_nickname", "test_content");

        Assertions.assertThrows(CustomException.class, () ->
                commentService.save(POST_ID, request));
    }

    @Test
    @DisplayName("case 2: 게시글과 댓글의 Tendency(투자 성향)이 일치하지 않는 경우, 예외처리")
    void save_with_Tendency_is_not_same_between_post_and_comment() {
        Tendency different = Tendency.SHEEP;    // @BeforeEach에서 저장된 post는 LION
        CommentRequest request = new CommentRequest(
                different, "test_nickname", "test_content");

        Assertions.assertThrows(CustomException.class, () ->
                commentService.save(POST_ID, request));
    }

    @Test
    @DisplayName("case 3: 정상 로직")
    void save() {
        // given
        Tendency tendency = POST_TENDENCY;
        String nickname = "새로운 댓글 작성자";
        String content = "댓글 작성 테스트";
        CommentRequest request = new CommentRequest(tendency, nickname, content);

        // when
        CommentResponse response = commentService.save(POST_ID, request);

        // then
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getPostId()).isEqualTo(POST_ID);
        assertThat(response.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("case 1: 게시글 id가 존재하지 않는 경우, 예외처리")
    void update_when_post_id_not_exists() {
        String updateContent = "Update the content";
        CommentUpdateRequest request = new CommentUpdateRequest(COMMENT_ID, COMMENT_NICKNAME,
                updateContent);
        Long postIdNotExists = 1000L;

        Assertions.assertThrows(CustomException.class, () ->
                commentService.update(postIdNotExists, request));
    }

    @Test
    @DisplayName("case 2: 게시글 id가 존재하지만, 댓글 id와 게시글 정보로 댓글 Entity를 찾지 못했을 때, 오류처리")
    void update_when_comment_is_not_in_db() {
        String updateContent = "Update the content";

        // 1: 댓글 id가 db에 없는 경우
        Long commentIdNotExists = 1000L;
        CommentUpdateRequest request = new CommentUpdateRequest(commentIdNotExists, COMMENT_NICKNAME,
                updateContent);
        Assertions.assertThrows(CustomException.class, () ->
                commentService.update(POST_ID, request));

        // 2: 댓글 id가 db에 존재하지만, 연관관계가 없는 게시글과 댓글을 조회하는 경우
        // 게시글, 댓글 하나씩 더 저장
        Post post = Post.builder()
                .nickname(POST_NICKNAME)
                .tendency(POST_TENDENCY)
                .content(POST_CONTENT)
                .viewCount(0L)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);
        Comment comment = Comment.builder()
                .nickname(COMMENT_NICKNAME)
                .content(COMMENT_CONTENT)
                .deleteStatus(DeleteStatus.F)
                .post(post)
                .build();
        post.getComments().add(comment);
        commentRepository.save(comment);

        Long commentId = 2L; // 이 부분이 변경됨.
        CommentUpdateRequest request2 = new CommentUpdateRequest(commentId, COMMENT_NICKNAME,
                updateContent);
        Assertions.assertThrows(CustomException.class, () ->
                commentService.update(POST_ID, request2));
    }

    @Test
    @DisplayName("case 3: 댓글 수정 권한이 없는 경우, 예외처리")
    void update_authorization_is_not_valid() {
        String nickname = "not_authorized";
        CommentUpdateRequest request = new CommentUpdateRequest(COMMENT_ID, nickname,
                COMMENT_CONTENT);

        Assertions.assertThrows(CustomException.class, () ->
                commentService.update(POST_ID, request));
    }

    @Test
    @DisplayName("case 4: 정상 수정 로직")
    void update() {
        // given
        String updateContent = "Update the content";
        CommentUpdateRequest request = new CommentUpdateRequest(COMMENT_ID, COMMENT_NICKNAME,
                updateContent);

        // when
        CommentResponse response = commentService.update(POST_ID, request);

        // then
        assertThat(response.getId()).isEqualTo(COMMENT_ID);
        assertThat(response.getPostId()).isEqualTo(POST_ID);
        assertThat(response.getNickname()).isEqualTo(COMMENT_NICKNAME);
        assertThat(response.getContent()).isEqualTo(updateContent); // here is updated
    }

    @Test
    @DisplayName("case 1: 게시글 id가 존재하지 않는 경우, 예외처리")
    void delete_when_post_id_not_exists() {
        CommentDeleteRequest request = new CommentDeleteRequest(COMMENT_ID, COMMENT_NICKNAME);
        Long postIdNotExists = 1000L;

        Assertions.assertThrows(CustomException.class, () ->
                commentService.delete(postIdNotExists, request));
    }

    @Test
    @DisplayName("case 2: 게시글 id가 존재하지만, 댓글 id와 게시글 정보로 댓글 Entity를 찾지 못했을 때, 오류처리")
    void delete_when_comment_is_not_in_db() {
        // 1: 댓글 id가 db에 없는 경우
        Long commentIdNotExists = 1000L;
        CommentDeleteRequest request = new CommentDeleteRequest(commentIdNotExists, COMMENT_NICKNAME);
        Assertions.assertThrows(CustomException.class, () ->
                commentService.delete(POST_ID, request));

        // 2: 댓글 id가 db에 존재하지만, 연관관계가 없는 게시글과 댓글을 조회하는 경우
        // 게시글, 댓글 하나씩 더 저장
        Post post = Post.builder()
                .nickname(POST_NICKNAME)
                .tendency(POST_TENDENCY)
                .content(POST_CONTENT)
                .viewCount(0L)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);
        Comment comment = Comment.builder()
                .nickname(COMMENT_NICKNAME)
                .content(COMMENT_CONTENT)
                .deleteStatus(DeleteStatus.F)
                .post(post)
                .build();
        post.getComments().add(comment);
        commentRepository.save(comment);

        Long commentId = 2L; // 이 부분이 변경됨.
        CommentDeleteRequest request2 = new CommentDeleteRequest(commentId, COMMENT_NICKNAME);
        Assertions.assertThrows(CustomException.class, () ->
                commentService.delete(POST_ID, request2));
    }

    @Test
    @DisplayName("case 3: 댓글 삭제 권한이 없는 경우, 예외처리")
    void delete_authorization_is_not_valid() {
        String nickname = "not_authorized";
        CommentDeleteRequest request = new CommentDeleteRequest(COMMENT_ID, nickname);

        Assertions.assertThrows(CustomException.class, () ->
                commentService.delete(POST_ID, request));
    }

    @Test
    @DisplayName("case 4: 정상 삭제 로직")
    void delete() {
        // given
        CommentDeleteRequest request = new CommentDeleteRequest(COMMENT_ID, COMMENT_NICKNAME);

        // when
        CommentDeleteResponse response = commentService.delete(POST_ID, request);

        // then
        assertThat(response.getId()).isEqualTo(COMMENT_ID);

        // DeleteStatus = 'T' 로 변경되었는지 검증
        Optional<Post> existingPostOptional = postRepository.findPostById(POST_ID);
        Optional<Comment> existingCommentOptional = commentRepository.findCommentByIdAndPost(COMMENT_ID, existingPostOptional.get());
        Comment existingComment = existingCommentOptional.get();

        assertThat(existingComment.getId()).isEqualTo(COMMENT_ID);
        assertThat(existingComment.getPost().getId()).isEqualTo(POST_ID);
        assertThat(existingComment.getDeleteStatus()).isEqualTo(DeleteStatus.T);
    }
}