package org.ai.roboadvisor.domain.community.repository;

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

    @Query("SELECT p FROM Post p WHERE p.tendency = :tendency AND p.deleteStatus = 'F'")
    Page<Post> findPostsByTendencyAndDeleteStatusIsFalse(@Param("tendency") Tendency tendency, Pageable pageable);
}
