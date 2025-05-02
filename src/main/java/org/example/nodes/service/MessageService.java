package org.example.nodes.service;

import org.example.nodes.model.Conversation;
import org.example.nodes.model.Message;
import org.example.nodes.model.User;
import org.example.nodes.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Conversation conversation, User sender, String content) {
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByConversation(Conversation conversation) {
        return messageRepository.findByConversation(conversation);
    }
}
