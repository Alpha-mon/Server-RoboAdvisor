package org.ai.roboadvisor.domain.user.repository;

import org.ai.roboadvisor.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNickname(String nickname);

    Optional<User> findUserByEmailAndPassword(String email, String password);

}
