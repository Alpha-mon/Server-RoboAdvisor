package org.ai.roboadvisor.domain.community.repository;

import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostById(Long id);

    List<Post> findAllByTendencyOrderByIdDesc(Tendency tendency);
}
