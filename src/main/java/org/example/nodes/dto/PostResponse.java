package org.example.nodes.dto;

import java.time.LocalDateTime;

public class PostResponse {
    private Long postId;
    private Long authorId;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
    private int likesCount;
    private int commentsCount;

    public PostResponse(Long postId, Long authorId, String authorName, String content,
                        LocalDateTime createdAt, int likesCount, int commentsCount) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }

    public Long getPostId() { return postId; }
    public Long getAuthorId() { return authorId; } // 👈 геттер для authorId
    public String getAuthorName() { return authorName; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getLikesCount() { return likesCount; }
    public int getCommentsCount() { return commentsCount; }
}
