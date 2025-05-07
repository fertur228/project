package org.example.nodes.service;

import org.example.nodes.dto.PostCreateRequest;
import org.example.nodes.dto.PostResponse;
import org.example.nodes.model.Post;
import org.example.nodes.model.User;
import org.example.nodes.repository.PostRepository;
import org.example.nodes.repository.UserRepository;
import org.example.nodes.utils.BadWordChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BadWordChecker badWordChecker;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       BadWordChecker badWordChecker) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.badWordChecker = badWordChecker;
    }

    /* ---------- CRUD ---------- */

    public void createPost(PostCreateRequest request) {
        if (badWordChecker.containsBadWords(request.getContent()))
            throw new RuntimeException("Пост содержит запрещённые слова");

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    /** Вернуть все посты + флаг likedByCurrentUser */
    public List<PostResponse> getAllPostsForUser(Long currentUserId) {
        return postRepository.findAll().stream()
                .map(p -> toDto(p, currentUserId))
                .collect(Collectors.toList());
    }

    public void updatePost(Long postId, PostCreateRequest request) {
        if (badWordChecker.containsBadWords(request.getContent()))
            throw new RuntimeException("Пост содержит запрещённые слова");

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));
        postRepository.delete(post);
    }

    /* ---------- лайки ---------- */

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

    /* ---------- поиск ---------- */

    public List<PostResponse> searchPosts(String query, Long currentUserId) {
        return postRepository.findAll().stream()
                .filter(p -> p.getContent().toLowerCase().contains(query.toLowerCase()) ||
                        p.getAuthor().getName().toLowerCase().contains(query.toLowerCase()))
                .map(p -> toDto(p, currentUserId))
                .collect(Collectors.toList());
    }

    /* ---------- mapper ---------- */

    private PostResponse toDto(Post post, Long currentUserId) {
        boolean liked = currentUserId != null &&
                post.getLikedBy().stream()
                        .anyMatch(u -> u.getId().equals(currentUserId));

        return new PostResponse(
                post.getPostId(),
                post.getAuthor().getId(),
                post.getAuthor().getName(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikesCount(),
                post.getCommentsCount(),
                liked
        );
    }
}
