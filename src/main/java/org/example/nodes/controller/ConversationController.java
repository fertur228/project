// src/main/java/org/example/nodes/controller/ConversationController.java
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

    public ConversationController(ConversationService conversationService,
                                  UserRepository userRepository) {
        this.conversationService = conversationService;
        this.userRepository = userRepository;
    }

    /** Создание (или получение существующей) беседы */
    @PostMapping
    public ResponseEntity<Conversation> createConversation(@RequestBody ConversationRequest request) {
        User u1 = userRepository.findById(request.getUser1Id())
                .orElseThrow(() -> new RuntimeException("Пользователь 1 не найден"));
        User u2 = userRepository.findById(request.getUser2Id())
                .orElseThrow(() -> new RuntimeException("Пользователь 2 не найден"));

        Conversation convo = conversationService.createConversation(u1, u2);
        return ResponseEntity.ok(convo);
    }

    /** Получить все беседы пользователя */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getConversationsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<Conversation> list = conversationService.getUserConversations(user);
        return ResponseEntity.ok(list);
    }

    /** Проверить, существует ли беседа между двумя пользователями */
    @GetMapping("/exist")
    public ResponseEntity<Boolean> exists(@RequestParam Long user1Id,
                                          @RequestParam Long user2Id) {
        User u1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("Пользователь 1 не найден"));
        User u2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("Пользователь 2 не найден"));
        boolean exists = conversationService.conversationExists(u1, u2);
        return ResponseEntity.ok(exists);
    }
}
