package org.ai.roboadvisor.domain.chat.repository;

import org.ai.roboadvisor.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    boolean existsChatByNickname(String nickname);

    @Query(value = "{ 'nickname' : ?0 }", delete = true)
    long deleteAllByNickname(String nickname);

}
