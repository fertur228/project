package org.example.nodes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subcomments")
public class Subcomment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcommentId;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;  // Ссылка на родительский комментарий

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Ссылка на пользователя, который написал подкомментарий

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private int likesCount = 0;

    @ManyToOne
    @JoinColumn(name = "parent_subcomment_id")  // Родительский подкомментарий (если есть)
    private Subcomment parentSubcomment;  // Ссылка на родительский подкомментарий

    // Геттеры и сеттеры
    public Long getSubcommentId() {
        return subcommentId;
    }

    public void setSubcommentId(Long subcommentId) {
        this.subcommentId = subcommentId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Subcomment getParentSubcomment() {
        return parentSubcomment;
    }

    public void setParentSubcomment(Subcomment parentSubcomment) {
        this.parentSubcomment = parentSubcomment;
    }
}
