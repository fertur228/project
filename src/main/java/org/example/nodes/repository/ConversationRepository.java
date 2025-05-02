package org.example.nodes.repository;

import org.example.nodes.model.Conversation;
import org.example.nodes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Найти все чаты, в которых участвует пользователь (user1 или user2)
    List<Conversation> findByUser1OrUser2(User user1, User user2);
}
