package org.example.nodes.service;

import org.example.nodes.dto.PostCreateRequest;
import org.example.nodes.dto.PostResponse;
import org.example.nodes.model.Post;
import org.example.nodes.model.User;
import org.example.nodes.repository.PostRepository;
import org.example.nodes.repository.UserRepository;
import org.example.nodes.service.StorageService;
import org.example.nodes.utils.BadWordChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис, отвечающий за CRUD‑операции с постами, файловое хранилище и лайки.
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BadWordChecker badWordChecker;
    private final StorageService storageService;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       BadWordChecker badWordChecker,
                       StorageService storageService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.badWordChecker = badWordChecker;
        this.storageService = storageService;
    }

    /* ──────────────── CREATE ──────────────── */

    /** JSON‑вариант (без медиа) остаётся для совместимости с фронтом. */
    public void createPost(PostCreateRequest request) {
        createPost(request.getAuthorId(), request.getContent(), null);
    }

    /** Основной вариант: текст + файл (может быть null). */
    public void createPost(Long authorId, String content, MultipartFile file) {
        validateContent(content);

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setMediaPath(storageService.store(file));   // может вернуть null
        postRepository.save(post);
    }

    /* ──────────────── READ ──────────────── */

    /** Вернуть все посты + флаг likedByCurrentUser. */
    public List<PostResponse> getAllPostsForUser(Long currentUserId) {
        return postRepository.findAll().stream()
                .map(p -> toDto(p, currentUserId))
                .collect(Collectors.toList());
    }

    /* ──────────────── UPDATE ──────────────── */

    /** JSON‑обновление без изменения файла. */
    public void updatePost(Long postId, PostCreateRequest request) {
        updatePost(postId, request.getContent(), null);
    }

    /** Обновить текст и (опционально) заменить файл. */
    public void updatePost(Long postId, String content, MultipartFile file) {
        validateContent(content);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        // если пришёл новый файл — удаляем старый и сохраняем новый
        if (file != null && !file.isEmpty()) {
            storageService.delete(post.getMediaPath());
            post.setMediaPath(storageService.store(file));
        }

        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    /* ──────────────── DELETE ──────────────── */

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        storageService.delete(post.getMediaPath());   // очищаем диск
        postRepository.delete(post);
    }

    /* ──────────────── LIKE / UNLIKE ──────────────── */

    @Transactional
    public int toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean removed = post.getLikedBy().remove(user);
        if (!removed) post.getLikedBy().add(user);

        post.recalcLikes();
        return postRepository.save(post).getLikesCount();
    }

    /* ──────────────── SEARCH ──────────────── */

    public List<PostResponse> searchPosts(String query, Long currentUserId) {
        String q = query.toLowerCase();
        return postRepository.findAll().stream()
                .filter(p -> p.getContent().toLowerCase().contains(q) ||
                        p.getAuthor().getName().toLowerCase().contains(q))
                .map(p -> toDto(p, currentUserId))
                .collect(Collectors.toList());
    }

    /* ──────────────── HELPERS ──────────────── */

    private void validateContent(String content) {
        if (badWordChecker.containsBadWords(content))
            throw new RuntimeException("Пост содержит запрещённые слова");
    }

    private PostResponse toDto(Post post, Long currentUserId) {
        boolean liked = currentUserId != null &&
                post.getLikedBy().stream()
                        .anyMatch(u -> u.getId().equals(currentUserId));

        return new PostResponse(
                post.getPostId(),
                post.getAuthor().getId(),
                post.getAuthor().getName(),
                post.getContent(),
                post.getMediaPath(),          // URL к изображению/видео
                post.getCreatedAt(),
                post.getLikesCount(),
                post.getCommentsCount(),
                liked
        );
    }
}
