package org.ai.roboadvisor.domain.community.service;

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

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("case 1: Tendency(투자 성향)이 적절하지 않은 타입이 온 경우, 예외처리")
    void save_with_TendencyIsNotValid() {
        PostRequest postRequestWithTendencyIsNotValid = new PostRequest(Tendency.TYPE_NOT_EXISTS,
                "test_nickname", "test_content");

        Assertions.assertThrows(CustomException.class, () ->
                postService.save(postRequestWithTendencyIsNotValid));
    }

    @Test
    @DisplayName("case 2: 정상 로직")
    void save() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        PostRequest postRequest = new PostRequest(lion, nickname, content);

        // when
        PostResponse response = postService.save(postRequest);

        // then
        assertThat(response.getTendency()).isEqualTo(lion);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getViewCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("case 1: id가 db에 존재하지 않는 경우, 예외처리")
    void getPostById_id_not_exists_inDB() {
        Post post = Post.builder()
                .nickname("test_user")
                .tendency(Tendency.LION)
                .content("test_content")
                .viewCount(0L)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        // when
        // 먼저 postRepository에 들어있는 Post Entity의 개수가 1개인지 검증
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(1L);

        // then
        // id = 2L 조회시 오류 발생(존재하지 않아서)
        Long num = 2L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.getPostById(num));
    }

    @Test
    @DisplayName("case 2: DeleteStatus = 'T'인 경우, 예외처리")
    void getPostById_post_is_deleted() {
        // save post with DeleteStatus = 'T'
        Post postWithDeleted = Post.builder()
                .nickname("test_user")
                .tendency(Tendency.LION)
                .content("test_content")
                .viewCount(0L)
                .deleteStatus(DeleteStatus.T)   // Pay attention to here!
                .build();
        postRepository.save(postWithDeleted);

        // when
        // 먼저 postRepository에 들어있는 Post Entity의 개수와, id가 실제로 1L 인지 검증
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(1L);

        // then
        Assertions.assertThrows(CustomException.class, () ->
                postService.getPostById(1L));
    }

    @Test
    @DisplayName("case 3: 정상 로직, 게시글에 포함된 댓글이 없는 경우")
    void getPostById_with_no_comments() {
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(0L)
                .deleteStatus(DeleteStatus.F)
                .build();

        postRepository.save(post);

        // when
        // 먼저 postRepository에 들어있는 Post Entity의 개수가 1개인지 검증
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(1L);

        // then
        // id = 1L 조회
        Long id = 1L;
        PostResponse response = postService.getPostById(id);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTendency()).isEqualTo(lion);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getContent()).isEqualTo(content);

        // 조회수 0 -> 1 증가 확인
        assertThat(response.getViewCount()).isEqualTo(1L);

        // Pay attention! Comment does not exist.
        assertThat(response.getComments()).isEmpty();
    }

    @Test
    @DisplayName("case 3-1: 정상 로직, 게시글에 포함된 댓글이 존재하는 경우")
    void getPostById_with_comments() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        int cmtCount = 3;
        List<Comment> comments = IntStream.rangeClosed(1, cmtCount)
                .mapToObj(c -> {
                    Comment comment = Comment.builder()
                            .nickname("cmt_" + c)
                            .content("cmt_content_" + c)
                            .deleteStatus(DeleteStatus.F)
                            .post(post)
                            .build();
                    post.getComments().add(comment); // 양방향 연관관계를 유지하기 위해 추가
                    return comment;
                })
                .toList();
        commentRepository.saveAll(comments);

        // when
        // 먼저 postRepository에 들어있는 Post Entity의 개수가 1개인지 검증
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(1L);

        // then
        // id = 1L 조회
        Long id = 1L;
        PostResponse response = postService.getPostById(id);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTendency()).isEqualTo(lion);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getContent()).isEqualTo(content);

        // 조회수 +1 증가 확인
        assertThat(response.getViewCount()).isEqualTo(viewCount + 1L);

        // Pay attention! Comment size is 3
        assertThat(response.getComments().size()).isEqualTo(cmtCount);
    }

    @Test
    @DisplayName("case 1: Tendency(투자 성향)이 적절하지 않은 타입이 온 경우, upd 예외처리")
    void update_tendency_is_not_valid() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        // update Request
        Tendency tendencyNotValid = Tendency.TYPE_NOT_EXISTS;
        String updateContent = "update Content!";
        PostRequest updRequest = new PostRequest(tendencyNotValid, nickname, updateContent);

        // then
        long postId = 1L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.update(postId, updRequest));
    }

    @Test
    @DisplayName("case 2: 사용자가 게시글 수정 권한이 없는 경우, 예외처리")
    void update_authorization_is_not_valid() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        // update Request
        String notAuthorized = "nickname_2";
        String updateContent = "update Content!";
        PostRequest updRequest = new PostRequest(lion, notAuthorized, updateContent);

        // then
        long postId = 1L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.update(postId, updRequest));
    }

    @Test
    @DisplayName("case 3: 정상 로직, 댓글은 없는 경우")
    void update_with_no_comments() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        // update Request
        String updateContent = "update Content!";
        PostRequest updRequest = new PostRequest(lion, nickname, updateContent);

        // when
        long postId = 1L;
        PostResponse response = postService.update(postId, updRequest);

        // then
        assertThat(response.getId()).isEqualTo(postId);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getContent()).isEqualTo(updateContent);
        assertThat(response.getComments()).isEmpty();

        // 조회수 +1 증가
        assertThat(response.getViewCount()).isEqualTo(viewCount + 1);
    }

    @Test
    @DisplayName("case 3-1: 정상 로직, 댓글이 존재하는 경우")
    void update_with_comments() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        int cmtCount = 3;
        List<Comment> comments = IntStream.rangeClosed(1, cmtCount)
                .mapToObj(c -> {
                    Comment comment = Comment.builder()
                            .nickname("cmt_" + c)
                            .content("cmt_content_" + c)
                            .deleteStatus(DeleteStatus.F)
                            .post(post)
                            .build();
                    post.getComments().add(comment); // 양방향 연관관계를 유지하기 위해 추가
                    return comment;
                })
                .toList();
        commentRepository.saveAll(comments);

        // update Request
        String updateContent = "update Content!";
        PostRequest updRequest = new PostRequest(lion, nickname, updateContent);

        // when
        long postId = 1L;
        PostResponse response = postService.update(postId, updRequest);

        // then
        assertThat(response.getId()).isEqualTo(postId);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getContent()).isEqualTo(updateContent);

        // 댓글 3개
        assertThat(response.getComments().size()).isEqualTo(cmtCount);

        // 조회수 +1 증가
        assertThat(response.getViewCount()).isEqualTo(viewCount + 1);
    }

    @Test
    @DisplayName("case 1: 사용자가 게시글 삭제 권한이 없는 경우, 예외처리")
    void delete_authorization_is_not_valid() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        String notAuthorized = "nickname_2";
        PostDeleteRequest deleteRequest = new PostDeleteRequest(notAuthorized);

        long postId = 1L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.delete(postId, deleteRequest));
    }

    @Test
    @DisplayName("case 2: 정상 로직. 게시글 삭제. 댓글은 존재하지 않는다고 가정")
    void delete_only_post() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        PostDeleteRequest deleteRequest = new PostDeleteRequest(nickname);

        // when
        long postId = 1L;
        PostDeleteResponse response = postService.delete(postId, deleteRequest);

        // then
        assertThat(response.getId()).isEqualTo(postId);

        // postId = 1L 인 db에 저장된 Post 엔티티의 deleteStatus 값이 T로 변경되었는지 검증
        // findPostById 메서드를 사용하면, deleteStatus = 'T'인 값은 가져오지 않으므로,
        // findAll() 메서드로 전체 조회 후 검증
        List<Post> existingPost = postRepository.findAll();
        assertThat(existingPost.size()).isEqualTo(1);
        assertThat(existingPost.get(0).getDeleteStatus()).isEqualTo(DeleteStatus.T);
    }

    @Test
    @DisplayName("case 2-1: 정상 로직. 게시글, 댓글 모두 삭제")
    void delete_post_and_comments() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = "test_nickname";
        String content = "test_content";
        long viewCount = 0L;

        Post post = Post.builder()
                .nickname(nickname)
                .tendency(lion)
                .content(content)
                .viewCount(viewCount)
                .deleteStatus(DeleteStatus.F)
                .build();
        postRepository.save(post);

        int cmtCount = 3;
        List<Comment> comments = IntStream.rangeClosed(1, cmtCount)
                .mapToObj(c -> {
                    Comment comment = Comment.builder()
                            .nickname("cmt_" + c)
                            .content("cmt_content_" + c)
                            .deleteStatus(DeleteStatus.F)
                            .post(post)
                            .build();
                    post.getComments().add(comment); // 양방향 연관관계를 유지하기 위해 추가
                    return comment;
                })
                .toList();
        commentRepository.saveAll(comments);

        PostDeleteRequest deleteRequest = new PostDeleteRequest(nickname);

        // when
        long postId = 1L;
        PostDeleteResponse response = postService.delete(postId, deleteRequest);

        // then
        assertThat(response.getId()).isEqualTo(postId);

        // postId = 1L 인 db에 저장된 Post 엔티티의 deleteStatus 값이 T로 변경되었는지 검증
        // findPostById 메서드를 사용하면, deleteStatus = 'T'인 값은 가져오지 않으므로,
        // findAll() 메서드로 전체 조회 후 검증
        List<Post> existingPost = postRepository.findAll();
        assertThat(existingPost.size()).isEqualTo(1);
        assertThat(existingPost.get(0).getDeleteStatus()).isEqualTo(DeleteStatus.T);

        // postId = 1L 인 db에 저장된 Comment 엔티티들의 deleteStatus 값이 T로 변경되었는지 검증
        List<Comment> existingComments = commentRepository.findAll();
        assertThat(existingComments.size()).isEqualTo(cmtCount);
        System.out.println("existingComments.get(0).getPost().getId() = " + existingComments.get(0).getPost().getId());
        System.out.println("existingComments.get(0).getDeleteStatus() = " + existingComments.get(0).getDeleteStatus());

        System.out.println("existingComments.get(1).getDeleteStatus() = " + existingComments.get(1).getDeleteStatus());
        System.out.println("existingComments.get(2).getDeleteStatus() = " + existingComments.get(2).getDeleteStatus());

        List<Comment> deleteStatusCnt = existingComments.stream()
                .filter(cmt -> cmt.getDeleteStatus() == DeleteStatus.T)
                .toList();
        assertThat(deleteStatusCnt.size()).isEqualTo(cmtCount);
    }
}