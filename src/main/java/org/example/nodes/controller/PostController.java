package org.example.nodes.controller;

import org.example.nodes.dto.PostCreateRequest;
import org.example.nodes.dto.PostResponse;
import org.example.nodes.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /* ──────────────── CREATE ──────────────── */

    /** Старый JSON‑вариант (без медиа) — оставили для обратной совместимости. */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPostJson(@RequestBody PostCreateRequest request) {
        postService.createPost(request);
        return ResponseEntity.ok("Пост успешно создан (без медиа)");
    }

    /** Новый вариант: multipart/form‑data (текст + файл). */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createPostMultipart(@RequestParam Long authorId,
                                                      @RequestParam String content,
                                                      @RequestPart(required = false) MultipartFile file) {
        postService.createPost(authorId, content, file);
        return ResponseEntity.ok("Пост успешно создан (media=" + (file != null) + ")");
    }

    /* ──────────────── READ ──────────────── */

    /** Получить ленту постов с флагом likedByCurrentUser.
     *  Пример:  GET /api/posts?userId=42
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Long userId) {
        return ResponseEntity.ok(postService.getAllPostsForUser(userId));
    }

    /* ──────────────── UPDATE ──────────────── */

    /** Обновление только текста (JSON). */
    @PutMapping(value = "/{postId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePostJson(@PathVariable Long postId,
                                                 @RequestBody PostCreateRequest request) {
        postService.updatePost(postId, request);
        return ResponseEntity.ok("Пост успешно обновлён (без изменения медиа)");
    }

    /** Обновить текст и (при необходимости) заменить файл. */
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePostMultipart(@PathVariable Long postId,
                                                      @RequestParam String content,
                                                      @RequestPart(required = false) MultipartFile file) {
        postService.updatePost(postId, content, file);
        return ResponseEntity.ok("Пост успешно обновлён (media=" + (file != null) + ")");
    }

    /* ──────────────── DELETE ──────────────── */

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Пост успешно удалён");
    }

    /* ──────────────── LIKE / UNLIKE ──────────────── */

    @PutMapping("/{postId}/like")
    public ResponseEntity<Integer> toggleLike(@PathVariable Long postId,
                                              @RequestParam Long userId) {
        int newCount = postService.toggleLike(postId, userId);
        return ResponseEntity.ok(newCount);
    }

    /* ──────────────── SEARCH ──────────────── */

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String query,
                                                          @RequestParam Long userId) {
        return ResponseEntity.ok(postService.searchPosts(query, userId));
    }
}
