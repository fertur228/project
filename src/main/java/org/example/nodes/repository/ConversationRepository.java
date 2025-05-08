package org.example.nodes.repository;

import org.example.nodes.model.Conversation;
import org.example.nodes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Уже был:
    List<Conversation> findByUser1OrUser2(User user1, User user2);

    // Новый метод — ищет единственную беседу между двумя юзерами
    @Query("""
      SELECT c FROM Conversation c
       WHERE (c.user1 = :u1 AND c.user2 = :u2)
          OR (c.user1 = :u2 AND c.user2 = :u1)
      """)
    Optional<Conversation> findByUsers(@Param("u1") User user1,
                                       @Param("u2") User user2);
}


