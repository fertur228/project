package org.example.nodes.service;

import org.example.nodes.dto.SubcommentRequest;
import org.example.nodes.dto.SubcommentUpdateRequest;
import org.example.nodes.model.Subcomment;
import org.example.nodes.model.Comment;
import org.example.nodes.model.User;
import org.example.nodes.repository.SubcommentRepository;
import org.example.nodes.repository.CommentRepository;
import org.example.nodes.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubcommentService {

    private final SubcommentRepository subcommentRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public SubcommentService(SubcommentRepository subcommentRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.subcommentRepository = subcommentRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Subcomment addSubcomment(SubcommentRequest request) {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Subcomment subcomment = new Subcomment();
        subcomment.setComment(comment);
        subcomment.setUser(user);
        subcomment.setContent(request.getContent());
        subcomment.setCreatedAt(LocalDateTime.now());

        if (request.getParentSubcommentId() != null) {
            Subcomment parentSubcomment = subcommentRepository.findById(request.getParentSubcommentId())
                    .orElseThrow(() -> new RuntimeException("Родительский подкомментарий не найден"));
            subcomment.setParentSubcomment(parentSubcomment);
        }

        return subcommentRepository.save(subcomment);
    }

    public List<Subcomment> getSubcommentsByComment(Long commentId) {
        return subcommentRepository.findByComment_CommentId(commentId);
    }

    public List<Subcomment> getSubcommentsBySubcomment(Long subcommentId) {
        return subcommentRepository.findByParentSubcomment_SubcommentId(subcommentId);
    }

    public Subcomment updateSubcomment(Long subcommentId, SubcommentUpdateRequest request) {
        Subcomment subcomment = subcommentRepository.findById(subcommentId)
                .orElseThrow(() -> new RuntimeException("Подкомментарий не найден"));
        subcomment.setContent(request.getContent());
        return subcommentRepository.save(subcomment);
    }

    public void deleteSubcomment(Long subcommentId) {
        Subcomment subcomment = subcommentRepository.findById(subcommentId)
                .orElseThrow(() -> new RuntimeException("Подкомментарий не найден"));
        subcommentRepository.delete(subcomment);
    }
}
