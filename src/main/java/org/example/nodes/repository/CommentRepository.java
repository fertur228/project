// src/main/java/org/example/nodes/repository/CommentRepository.java
package org.example.nodes.repository;

import org.example.nodes.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_PostId(Long postId);

    // ← новый метод для массового удаления комментариев по postId
    void deleteByPost_PostId(Long postId);
}
