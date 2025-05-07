package org.example.nodes.controller;

import org.example.nodes.dto.PostCreateRequest;
import org.example.nodes.dto.PostResponse;
import org.example.nodes.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /* ---------- CRUD ---------- */

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostCreateRequest request) {
        postService.createPost(request);
        return ResponseEntity.ok("Пост успешно создан");
    }

    /**
     * Получить ленту постов с флагом likedByCurrentUser.
     * Пример:  GET /api/posts?userId=42
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Long userId) {
        return ResponseEntity.ok(postService.getAllPostsForUser(userId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId,
                                             @RequestBody PostCreateRequest request) {
        postService.updatePost(postId, request);
        return ResponseEntity.ok("Пост успешно обновлён");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Пост успешно удалён");
    }

    /* ---------- лайк / дизлайк ---------- */
    @PutMapping("/{postId}/like")
    public ResponseEntity<Integer> toggleLike(@PathVariable Long postId,
                                              @RequestParam Long userId) {
        int newCount = postService.toggleLike(postId, userId);
        return ResponseEntity.ok(newCount);
    }

    /* ---------- поиск ---------- */
    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String query,
                                                          @RequestParam Long userId) {
        return ResponseEntity.ok(postService.searchPosts(query, userId));
    }
}
