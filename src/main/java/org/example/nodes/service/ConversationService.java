package org.example.nodes.service;
import org.example.nodes.model.Conversation;
import org.example.nodes.model.User;
import org.example.nodes.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation createConversation(User user1, User user2) {
        // Проверяем, есть ли уже такой чат
        return conversationRepository.findByUsers(user1, user2)
                // если есть — возвращаем его
                .orElseGet(() -> {
                    Conversation conversation = new Conversation();
                    conversation.setUser1(user1);
                    conversation.setUser2(user2);
                    conversation.setCreatedAt(LocalDateTime.now());
                    return conversationRepository.save(conversation);
                });
    }

    public List<Conversation> getUserConversations(User user) {
        return conversationRepository.findByUser1OrUser2(user, user);
    }

    // Опционально: вынести в сервис метод проверки
    public boolean conversationExists(User user1, User user2) {
        return conversationRepository.findByUsers(user1, user2).isPresent();
    }
}
