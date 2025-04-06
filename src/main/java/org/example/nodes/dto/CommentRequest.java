package org.example.nodes.dto;

public class CommentRequest {
    private Long postId;
    private Long userId;
    private String content;

    // Геттеры и сеттеры
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
