package org.ai.roboadvisor.domain.community.repository;

import org.ai.roboadvisor.domain.community.entity.Comment;
import org.ai.roboadvisor.domain.community.entity.DeleteStatus;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentByIdAndPost(Long id, Post post);

    @Modifying
    @Query("UPDATE Comment c SET c.deleteStatus = :deleteStatus WHERE c.post.id = :postId")
    void markCommentsAsDeleted(@Param("postId") Long postId, @Param("deleteStatus") DeleteStatus deleteStatus);

}
