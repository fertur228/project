package org.example.nodes.controller;

import org.example.nodes.dto.MessageRequest;
import org.example.nodes.model.Conversation;
import org.example.nodes.model.Message;
import org.example.nodes.model.User;
import org.example.nodes.repository.ConversationRepository;
import org.example.nodes.repository.UserRepository;
import org.example.nodes.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    // Инжектируем все репозитории
    public MessageController(MessageService messageService,
                             ConversationRepository conversationRepository,
                             UserRepository userRepository) {
        this.messageService = messageService;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    // Метод для отправки сообщения
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest request) {
        // Получаем чат и отправителя
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Отправляем сообщение через сервис
        Message message = messageService.sendMessage(conversation, sender, request.getContent());
        return ResponseEntity.ok(message);
    }

    // Метод для получения сообщений в чате
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));

        // Получаем все сообщения для чата
        List<Message> messages = messageService.getMessagesByConversation(conversation);
        return ResponseEntity.ok(messages);
    }
}