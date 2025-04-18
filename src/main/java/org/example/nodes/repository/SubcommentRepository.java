package org.example.nodes.repository;

import org.example.nodes.model.Subcomment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcommentRepository extends JpaRepository<Subcomment, Long> {
    List<Subcomment> findByComment_CommentId(Long commentId);  // Получение подкомментариев для комментария

    List<Subcomment> findByParentSubcomment_SubcommentId(Long subcommentId);  // Получение подкомментариев для подкомментария
}
