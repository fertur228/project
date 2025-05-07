package org.example.nodes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String content;

    /** Относительный путь к файлу типа /uploads/posts/uuid.jpg */
    private String mediaPath;  // 🔴 НОВОЕ

    @Column(name = "date", nullable = false)
    private LocalDateTime createdAt;

    /* ---------- лайки ---------- */
    @ManyToMany
    @JoinTable(name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
    private Set<User> likedBy = new HashSet<>();

    private int likesCount = 0;      // кэш для быстрого вывода
    private int commentsCount = 0;

    /* ---------- геттеры/сеттеры ---------- */
    public Long getPostId()                { return postId; }
    public void setPostId(Long postId)     { this.postId = postId; }

    public User getAuthor()                { return author; }
    public void setAuthor(User author)     { this.author = author; }

    public String getContent()             { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt()    { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getLikesCount()             { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public int getCommentsCount()          { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }

    public Set<User> getLikedBy()          { return likedBy; }
    public void setLikedBy(Set<User> likedBy) { this.likedBy = likedBy; }

    /* пересчитать кэш лайков */
    public void recalcLikes()              { this.likesCount = likedBy.size(); }

    // Новые методы для mediaPath
    public String getMediaPath()              { return mediaPath; }
    public void setMediaPath(String mediaPath){ this.mediaPath = mediaPath; }
}
