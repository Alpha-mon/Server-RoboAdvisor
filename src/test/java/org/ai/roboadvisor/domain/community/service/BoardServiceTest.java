package org.ai.roboadvisor.domain.community.service;

import org.ai.roboadvisor.domain.community.dto.response.BoardResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private final Long POST_ID = 1L;
    private final int parentCmtCount = 6;
    private final int childCmtCount = 8;

    @BeforeEach
    void setUp() {
        int postCount = 13;
        IntStream.rangeClosed(1, postCount).forEach(p -> {
            Post post = Post.builder()
                    .nickname("test_" + p)
                    .tendency(Tendency.LION)
                    .content("test_content_" + p)
                    .viewCount(0L)
                    .deleteStatus(DeleteStatus.F)
                    .build();
            postRepository.save(post);

            // 게시글 id = 1L에 대한 댓글 생성
            if (p == 1) {
                IntStream.rangeClosed(1, parentCmtCount).forEach(j -> {
                    Comment comment = Comment.builder()
                            .nickname("cmt_" + j)
                            .content("cmt_content_" + j)
                            .deleteStatus(DeleteStatus.F)
                            .post(post)              // 양방향 연관관계 설정
                            .build();
                    post.getComments().add(comment); // 양방향 연관관계를 유지하기 위해 추가
                    commentRepository.save(comment);

                    // 게시글 id = 1L, 댓글 id = 1L인 대댓글 생성, DeleteStatus = 'F'
                    if (j == 1) {
                        IntStream.rangeClosed(1, childCmtCount).forEach(k -> {
                            Comment childComment = Comment.builder()
                                    .nickname("cmt_parent_" + k)
                                    .content("cmt_content_parent_" + k)
                                    .deleteStatus(DeleteStatus.F)   // 삭제되지 않은 댓글
                                    .parent(comment)         // 부모 댓글 설정
                                    .post(post)              // 양방향 연관관계 설정
                                    .build();
                            post.getComments().add(childComment); // 양방향 연관관계를 유지하기 위해 추가
                            commentRepository.save(childComment);
                        });
                    }
                });
            }

            // 게시글 id = 2L에 대한 댓글 생성
            if (p == 2) {
                IntStream.rangeClosed(1, parentCmtCount).forEach(j -> {
                    Comment comment = Comment.builder()
                            .nickname("cmt_" + j)
                            .content("cmt_content_" + j)
                            .deleteStatus(DeleteStatus.F)
                            .post(post)              // 양방향 연관관계 설정
                            .build();
                    post.getComments().add(comment); // 양방향 연관관계를 유지하기 위해 추가
                    commentRepository.save(comment);

                    // 게시글 id = 2L, 댓글 id = 2L인 대댓글 생성, DeleteStatus = 'F'
                    if (j == 1) {
                        IntStream.rangeClosed(1, childCmtCount).forEach(k -> {
                            Comment childComment = Comment.builder()
                                    .nickname("cmt_parent_" + k)
                                    .content("cmt_content_parent_" + k)
                                    .deleteStatus(DeleteStatus.T)   // 삭제된 댓글
                                    .parent(comment)         // 부모 댓글 설정
                                    .post(post)              // 양방향 연관관계 설정
                                    .build();
                            post.getComments().add(childComment); // 양방향 연관관계를 유지하기 위해 추가
                            commentRepository.save(childComment);
                        });
                    }
                });
            }

        });
    }

    @Test
    @DisplayName("Tendency(투자 성향)이 적절하지 않은 타입이 온 경우, 예외처리")
    void getAllPostsByType_TendencyIsNotValid() {
        // given
        Tendency tendency = Tendency.TYPE_NOT_EXISTS;
        int page = 0;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        // when: Call the method
        Assertions.assertThrows(CustomException.class, () ->
                boardService.getAllPostsByTendency(tendency, pageable));
    }

    @Test
    @DisplayName("page 0, size 10 -> 10개 데이터를 리턴한다")
    void getAllPostsByType_page_is_0() {
        // given
        Tendency tendency = Tendency.LION;
        int page = 0;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        // when: Call the method
        List<BoardResponse> boardResponses = boardService.getAllPostsByTendency(tendency, pageable);

        // then: Verify the results
        assertNotNull(boardResponses);
        assertThat(boardResponses.size()).isEqualTo(pageSize);
    }

    @Test
    @DisplayName("id가 1인 게시글에 속한 댓글이 6개, 대댓글이 8개일 때, commentCount가 정상적으로 6+8이 나오는지 검증")
    void getAllPostsByType_id_1_count_comments() {
        // given
        Tendency tendency = Tendency.LION;
        int page = 0;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        // when: Call the method
        List<BoardResponse> boardResponses = boardService.getAllPostsByTendency(tendency, pageable);

        // then
        // 먼저 실제로 게시글 id 1에 대한 전체 댓글 수가 14개인지 검증(DeleteStatus를 고려하지 않고)
        Long postId1 = POST_ID;
        Optional<Post> postId1optional = postRepository.findPostById(postId1);
        List<Comment> comments = commentRepository.findCommentsByPost(postId1optional.get());
        assertThat(comments.size()).isEqualTo(parentCmtCount + childCmtCount);

        // id ASC 정렬이므로, 리스트의 인덱스가 작은 값이 id가 작다.
        // DeleteStatus 고려
        BoardResponse boardResponseId1 = boardResponses.get(0);
        assertThat(boardResponseId1.getId()).isEqualTo(postId1);
        assertThat(boardResponseId1.getCommentCount()).isEqualTo(parentCmtCount + childCmtCount);
    }

    @Test
    @DisplayName("id가 2인 게시글에 속한 댓글이 6개, 삭제된 대댓글이 8개일 때, commentCount가 정상적으로 6이 나오는지 검증")
    void getAllPostsByType_id_2_count_comments() {
        // given
        Tendency tendency = Tendency.LION;
        int page = 0;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        // when: Call the method
        List<BoardResponse> boardResponses = boardService.getAllPostsByTendency(tendency, pageable);

        // then
        // 먼저 실제로 게시글 id 2에 대한 전체 댓글 수가 14개인지 검증(DeleteStatus를 고려하지 않고)
        Long postId2 = 2L;
        Optional<Post> postId2optional = postRepository.findPostById(postId2);
        List<Comment> comments = commentRepository.findCommentsByPost(postId2optional.get());
        assertThat(comments.size()).isEqualTo(parentCmtCount + childCmtCount);

        // id ASC 정렬이므로, 리스트의 인덱스가 작은 값이 id가 작다.
        // DeleteStatus 고려
        BoardResponse boardResponseId2 = boardResponses.get(1);
        assertThat(boardResponseId2.getId()).isEqualTo(postId2);
        assertThat(boardResponseId2.getCommentCount()).isEqualTo(parentCmtCount); // DeleteStatus.T인 8개는 무시한다.
    }

    @Test
    @DisplayName("page 1, size 10 -> 3개 데이터를 리턴한다")
    void getAllPostsByType_page_is_1() {
        // given
        Tendency tendency = Tendency.LION;
        int page = 1;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        // when: Call the method
        List<BoardResponse> boardResponses = boardService.getAllPostsByTendency(tendency, pageable);

        // then: Verify the results
        assertNotNull(boardResponses);
        assertThat(boardResponses.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("page 2, size 10 -> 0개 데이터를 리턴한다")
    void getAllPostsByType_page_is_2() {
        // given
        Tendency tendency = Tendency.LION;
        int page = 2;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        // when: Call the method
        List<BoardResponse> boardResponses = boardService.getAllPostsByTendency(tendency, pageable);

        // then: Verify the results
        assertNotNull(boardResponses);
        assertThat(boardResponses.size()).isZero();
    }

}