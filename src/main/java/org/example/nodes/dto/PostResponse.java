package org.example.nodes.dto;

import java.time.LocalDateTime;

/**
 * DTO для отдачи поста на фронт.
 * Содержит всю информацию, которая нужна в ленте:
 *  - кто автор
 *  - текст поста
 *  - URL к загруженному медиа‑файлу (если он есть)
 *  - счётчики лайков / комментариев
 *  - флаг «лайкнул ли текущий пользователь»
 */
public class PostResponse {

    private Long postId;
    private Long authorId;
    private String authorName;
    private String content;
    private String mediaUrl;                 // 🔴 новое поле
    private LocalDateTime createdAt;
    private int likesCount;
    private int commentsCount;
    private boolean likedByCurrentUser;

    public PostResponse(Long postId,
                        Long authorId,
                        String authorName,
                        String content,
                        String mediaUrl,
                        LocalDateTime createdAt,
                        int likesCount,
                        int commentsCount,
                        boolean likedByCurrentUser) {
        this.postId   = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.createdAt = createdAt;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    /* ---------- геттеры ---------- */

    public Long getPostId()                { return postId; }
    public Long getAuthorId()              { return authorId; }
    public String getAuthorName()          { return authorName; }
    public String getContent()             { return content; }
    public String getMediaUrl()            { return mediaUrl; }
    public LocalDateTime getCreatedAt()    { return createdAt; }
    public int getLikesCount()             { return likesCount; }
    public int getCommentsCount()          { return commentsCount; }
    public boolean isLikedByCurrentUser()  { return likedByCurrentUser; }
}
