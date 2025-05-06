package org.example.nodes.service;

import org.example.nodes.dto.CommentRequest;
import org.example.nodes.dto.CommentUpdateRequest;
import org.example.nodes.model.Comment;
import org.example.nodes.model.Post;
import org.example.nodes.model.User;
import org.example.nodes.repository.CommentRepository;
import org.example.nodes.repository.PostRepository;
import org.example.nodes.repository.UserRepository;
import org.example.nodes.utils.BadWordChecker; // Убедитесь, что импорт правильный
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BadWordChecker badWordChecker;  // Внедрение зависимости

    // Конструктор с внедрением BadWordChecker
    public CommentService(CommentRepository commentRepository, PostRepository postRepository,
                          UserRepository userRepository, BadWordChecker badWordChecker) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.badWordChecker = badWordChecker;  // Инициализация
    }

    public Comment addComment(CommentRequest request) {
        // Проверка на наличие плохих слов с использованием экземпляра BadWordChecker
        if (badWordChecker.containsBadWords(request.getContent())) {
            throw new RuntimeException("Комментарий содержит неприемлемые слова");
        }

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPost_PostId(postId);
    }

    public Comment updateComment(Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Проверка на наличие плохих слов при обновлении с использованием экземпляра BadWordChecker
        if (badWordChecker.containsBadWords(request.getContent())) {
            throw new RuntimeException("Комментарий содержит неприемлемые слова");
        }

        comment.setContent(request.getContent());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
