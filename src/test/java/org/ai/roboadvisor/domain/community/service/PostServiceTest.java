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
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private final String USER_NICKNAME = "comment_user";
    private final Tendency USER_TENDENCY = Tendency.LION;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("test@t.com")
                .nickname(USER_NICKNAME)
                .tendency(USER_TENDENCY)
                .gender("male")
                .password("3fasfzxvae")
                .birth(LocalDate.now())
                .career(null)
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("case 1: DB에 없는 사용자 닉네임이 들어오는 경우, 예외처리")
    void save_with_user_not_existed_in_db() {
        String userNickname = "not_in_db";
        PostRequest request = new PostRequest(userNickname, "test_content");

        Assertions.assertThrows(CustomException.class, () ->
                postService.save(request));
    }

    @Test
    @DisplayName("case 2: 정상 로직")
    void save() {
        // given
        String nickname = USER_NICKNAME;
        String content = "test_content";
        PostRequest postRequest = new PostRequest(nickname, content);

        // when
        PostResponse response = postService.save(postRequest);

        // then
        assertThat(response.getTendency()).isEqualTo(USER_TENDENCY);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getViewCount()).isEqualTo(0L);
        assertThat(response.getComments()).isEmpty();
    }

    @Test
    @DisplayName("case 1: post id가 db에 존재하지 않는 경우, 예외처리")
    void getPostById_post_not_exists_in_DB() {
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
        // id = 100L 조회시 오류 발생(존재하지 않아서)
        Long idNotInDB = 100L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.getPostById(idNotInDB));
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

        // Pay attention! Comment size is cmtCount
        assertThat(response.getComments().size()).isEqualTo(cmtCount);
    }

    @Test
    @DisplayName("case 3-2: 정상 로직, 게시글에 포함된 댓글 및 대댓글이 존재하는 경우")
    void getPostById_with_Comments_and_childComments() {
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
        int childCmtCountWithNotDeleted = 6;
        int childCmtCountWithDeleted = 4;
        List<Comment> comments = IntStream.rangeClosed(1, cmtCount)
                .mapToObj(c -> {
                    Comment comment = Comment.builder()
                            .nickname("cmt_" + c)
                            .content("cmt_content_" + c)
                            .deleteStatus(DeleteStatus.F)
                            .post(post)
                            .build();
                    post.getComments().add(comment); // 양방향 연관관계를 유지하기 위해 추가

                    // 게시글 id = 1L, 댓글 id = 1L인 대댓글 생성. DeleteStatus = 'F'
                    if (c == 1) {
                        IntStream.rangeClosed(1, childCmtCountWithNotDeleted)
                                .forEach(c2 -> {
                                    Comment childComment = Comment.builder()
                                            .nickname("cmt_child_" + c2)
                                            .content("cmt_content_child_" + c2)
                                            .deleteStatus(DeleteStatus.F)
                                            .parent(comment)             // 부모 댓글 추가
                                            .post(post)
                                            .build();
                                    post.getComments().add(childComment); // 양방향 연관관계를 유지하기 위해 추가
                                    commentRepository.save(childComment);
                                });
                    }

                    // 게시글 id = 1L, 댓글 id = 2L인 대댓글 생성. DeleteStatus = 'T'
                    if (c == 2) {
                        IntStream.rangeClosed(1, childCmtCountWithDeleted)
                                .forEach(c2 -> {
                                    Comment childComment = Comment.builder()
                                            .nickname("cmt_child_" + c2)
                                            .content("cmt_content_child_" + c2)
                                            .deleteStatus(DeleteStatus.T)
                                            .parent(comment)             // 부모 댓글 추가
                                            .post(post)
                                            .build();
                                    post.getComments().add(childComment); // 양방향 연관관계를 유지하기 위해 추가
                                    commentRepository.save(childComment);
                                });
                    }

                    return comment;
                })
                .toList();
        commentRepository.saveAll(comments);

        // when
        // 먼저 postRepository에 들어있는 Post Entity의 개수가 1개인지 검증
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(1L);

        // 댓글 + 대댓글 총합이 cmtCount + childCmtCountWithNotDeleted + childCmtCountWithDeleted 인지 검증(DeleteStatus 무시)
        List<Comment> resultComments = commentRepository.findAll();
        assertThat(resultComments.size()).isEqualTo(cmtCount + childCmtCountWithNotDeleted + childCmtCountWithDeleted);

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

        // Pay attention! 댓글 개수 확인(DeleteStatus 가 반영되는지)
        assertThat(response.getComments().size()).isEqualTo(cmtCount + childCmtCountWithNotDeleted);
    }

    @Test
    @DisplayName("case 1: DB에 없는 사용자 닉네임이 들어오는 경우, 예외처리")
    void update_with_user_not_existed_in_db() {
        String userNickname = "not_in_db";
        PostRequest request = new PostRequest(userNickname, "test_content");

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

        // then
        long postId = 1L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.update(postId, request));
    }

    @Test
    @DisplayName("case 2: DB에 없는 게시글 id가 요청으로 들어오는 경우, 예외처리")
    void update_with_post_not_existed_in_db() {
        String nickname = USER_NICKNAME;
        PostRequest request = new PostRequest(nickname, "test_content");

        Tendency lion = USER_TENDENCY;
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

        // when
        // 먼저 postRepository에 들어있는 Post Entity의 개수가 1개인지 검증
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(1L);

        // then
        // id = 100L 조회시 오류 발생(존재하지 않아서)
        // then
        long idNotInDB = 100L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.update(idNotInDB, request));
    }

    @Test
    @DisplayName("case 3: 사용자가 게시글 수정 권한이 없는 경우, 예외처리")
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

        // 권한 있는 사용자도 같이 저장
        User user = User.builder()
                .email("test@tt.com")
                .nickname(nickname)
                .tendency(lion)
                .gender("male")
                .password("3fasfzxvasdfaszxv1ae")
                .birth(LocalDate.now())
                .career(null)
                .build();
        userRepository.save(user);

        // update Request
        String notAuthorized = USER_NICKNAME;
        String updateContent = "update Content!";
        PostRequest updRequest = new PostRequest(notAuthorized, updateContent);

        // then
        long postId = 1L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.update(postId, updRequest));
    }

    @Test
    @DisplayName("case 4: 정상 로직, 댓글은 없는 경우")
    void update_with_no_comments() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = USER_NICKNAME;
        String content = "new content";
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
        PostRequest updRequest = new PostRequest(nickname, updateContent);

        // when
        long postId = 1L;
        PostResponse response = postService.update(postId, updRequest);

        // then
        assertThat(response.getId()).isEqualTo(postId);
        assertThat(response.getTendency()).isEqualTo(USER_TENDENCY);
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getContent()).isEqualTo(updateContent);
        assertThat(response.getComments()).isEmpty();

        // 조회수 +1 증가
        assertThat(response.getViewCount()).isEqualTo(viewCount + 1);
    }

    @Test
    @DisplayName("case 4-1: 정상 로직, 댓글이 존재하는 경우")
    void update_with_comments() {
        // given
        Tendency lion = Tendency.LION;
        String nickname = USER_NICKNAME;
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
        PostRequest updRequest = new PostRequest(nickname, updateContent);

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
    @DisplayName("case 4-2: update 시에 BaseEntity의 update 시간이 변경되는 지 검증. 정상 로직, 댓글은 없는 경우")
    void update_check_time_updated() throws InterruptedException {
        // given
        Tendency lion = Tendency.LION;
        String nickname = USER_NICKNAME;
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

        // get saved entity
        Optional<Post> savedPost = postRepository.findPostById(1L);
        LocalDateTime updatedTime = savedPost.get().getModifiedDateTime();

        Thread.sleep(1000L);

        // update Request
        String updateContent = "update Content!";
        PostRequest updRequest = new PostRequest(nickname, updateContent);

        // when
        long postId = 1L;
        PostResponse response = postService.update(postId, updRequest);

        // then
        Optional<Post> savedPost2 = postRepository.findPostById(1L);
        LocalDateTime updatedTime2 = savedPost2.get().getModifiedDateTime();

        assertThat(updatedTime).isBeforeOrEqualTo(updatedTime2);
    }

    @Test
    @DisplayName("case 1: DB에 없는 게시글 id가 요청으로 들어오는 경우, 예외처리")
    void delete_with_post_not_existed_in_db() {
        String nickname = USER_NICKNAME;
        PostDeleteRequest deleteRequest = new PostDeleteRequest(nickname);

        Tendency lion = USER_TENDENCY;
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

        // when
        // 먼저 postRepository에 들어있는 Post Entity의 개수가 1개인지 검증
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(1L);

        // then
        // id = 100L 조회시 오류 발생(존재하지 않아서)
        // then
        long idNotInDB = 100L;
        Assertions.assertThrows(CustomException.class, () ->
                postService.delete(idNotInDB, deleteRequest));
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