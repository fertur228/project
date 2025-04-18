package org.example.nodes.dto;

public class SubcommentRequest {
    private Long commentId;  // ID родительского комментария
    private Long userId;     // ID пользователя, который оставляет подкомментарий
    private String content;  // Текст подкомментария
    private Long parentSubcommentId;  // ID родительского подкомментария (если это ответ на подкомментарий)

    // Геттеры и сеттеры
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentSubcommentId() {
        return parentSubcommentId;
    }

    public void setParentSubcommentId(Long parentSubcommentId) {
        this.parentSubcommentId = parentSubcommentId;
    }
}
