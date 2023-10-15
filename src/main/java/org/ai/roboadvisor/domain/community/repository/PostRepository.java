package org.ai.roboadvisor.domain.community.repository;

import org.ai.roboadvisor.domain.community.dto.response.BoardResponse;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.deleteStatus = 'F'")
    Optional<Post> findPostById(@Param("id") Long id);

    @Query("SELECT new org.ai.roboadvisor.domain.community.dto.response.BoardResponse" +
            "(p.id, p.tendency, p.nickname, p.content, p.createdDateTime, p.viewCount, COUNT(c.id)) " +
            "FROM Post p " +
            "LEFT JOIN Comment c ON p.id = c.post.id AND c.deleteStatus = 'F' " +
            "WHERE p.tendency = :tendency " +
            "GROUP BY p.id")
    Page<BoardResponse> findPostsWithCommentCountByTendency(@Param("tendency") Tendency tendency, Pageable pageable);
}
