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

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostCreateRequest request) {
        postService.createPost(request);
        return ResponseEntity.ok("Пост успешно создан");
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // Эндпоинт для редактирования поста
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostCreateRequest request) {
        postService.updatePost(postId, request);
        return ResponseEntity.ok("Пост успешно обновлен");
    }

    // Эндпоинт для удаления поста
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Пост успешно удален");
    }

    // Новый эндпоинт для добавления лайка
    @PutMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        try {
            postService.incrementLikes(postId);  // Увеличиваем количество лайков
            return ResponseEntity.ok("Лайк добавлен");  // Ответ с успешным сообщением
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Пост не найден");  // В случае ошибки (например, пост не найден)
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam("query") String query) {
        return ResponseEntity.ok(postService.searchPosts(query));
    }

}
