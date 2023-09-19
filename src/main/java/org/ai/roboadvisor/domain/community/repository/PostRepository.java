package org.ai.roboadvisor.domain.community.repository;

import org.ai.roboadvisor.domain.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostById(Long id);

}
