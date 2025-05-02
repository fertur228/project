package org.example.nodes.controller;

import org.example.nodes.dto.ConversationRequest;
import org.example.nodes.model.Conversation;
import org.example.nodes.model.User;
import org.example.nodes.repository.UserRepository;
import org.example.nodes.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final UserRepository userRepository;

    // Инжектируем ConversationService и UserRepository
    public ConversationController(ConversationService conversationService, UserRepository userRepository) {
        this.conversationService = conversationService;
        this.userRepository = userRepository;
    }

    // Создание чата между двумя пользователями через JSON
    @PostMapping
    public ResponseEntity<Conversation> createConversation(@RequestBody ConversationRequest request) {
        // Получаем пользователей
        User user1 = userRepository.findById(request.getUser1Id())
                .orElseThrow(() -> new RuntimeException("Пользователь 1 не найден"));
        User user2 = userRepository.findById(request.getUser2Id())
                .orElseThrow(() -> new RuntimeException("Пользователь 2 не найден"));

        // Создаем чат через сервис
        Conversation conversation = conversationService.createConversation(user1, user2);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getConversationsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Conversation> conversations = conversationService.getUserConversations(user);
        return ResponseEntity.ok(conversations);
    }

}