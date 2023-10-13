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

    private final int cmtCount = 6;

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

            if (p == 1) {
                IntStream.rangeClosed(1, cmtCount).forEach(j -> {
                    Comment comment = Comment.builder()
                            .nickname("cmt_" + j)
                            .content("cmt_content_" + j)
                            .deleteStatus(DeleteStatus.F)
                            .post(post)              // 양방향 연관관계 설정
                            .build();
                    post.getComments().add(comment); // 양방향 연관관계를 유지하기 위해 추가
                    commentRepository.save(comment);
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
                boardService.getAllPostsByType(tendency, pageable));
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
        List<BoardResponse> boardResponses = boardService.getAllPostsByType(tendency, pageable);

        // then: Verify the results
        assertNotNull(boardResponses);
        assertThat(boardResponses.size()).isEqualTo(pageSize);
    }

    @Test
    @DisplayName("id가 1인 게시글에 속한 댓글이 6개일 때, commentCount가 정상적으로 6이 나오는지 검증")
    void getAllPostsByType_count_comments() {
        // given
        Tendency tendency = Tendency.LION;
        int page = 0;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        // when: Call the method
        List<BoardResponse> boardResponses = boardService.getAllPostsByType(tendency, pageable);

        // then
        // id ASC 정렬이므로, 리스트의 인덱스가 작은 값이 id가 작다.
        BoardResponse boardResponseId1 = boardResponses.get(0);
        assertThat(boardResponseId1.getId()).isEqualTo(1L);
        assertThat(boardResponseId1.getCommentCount()).isEqualTo(cmtCount);
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
        List<BoardResponse> boardResponses = boardService.getAllPostsByType(tendency, pageable);

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
        List<BoardResponse> boardResponses = boardService.getAllPostsByType(tendency, pageable);

        // then: Verify the results
        assertNotNull(boardResponses);
        assertThat(boardResponses.size()).isZero();
    }

}