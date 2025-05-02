package org.example.nodes.repository;

import org.example.nodes.model.Conversation;
import org.example.nodes.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Получить все сообщения в чате (по conversation_id)
    List<Message> findByConversation(Conversation conversation);
}

