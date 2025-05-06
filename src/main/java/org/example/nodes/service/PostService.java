package org.example.nodes.service;

import org.example.nodes.dto.PostCreateRequest;
import org.example.nodes.dto.PostResponse;
import org.example.nodes.model.Post;
import org.example.nodes.model.User;
import org.example.nodes.repository.PostRepository;
import org.example.nodes.repository.UserRepository;
import org.example.nodes.utils.BadWordChecker; // Импортируем BadWordChecker
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BadWordChecker badWordChecker; // Внедряем зависимость

    // Конструктор с внедрением BadWordChecker
    public PostService(PostRepository postRepository, UserRepository userRepository, BadWordChecker badWordChecker) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.badWordChecker = badWordChecker; // Инициализация
    }

    // Метод для создания поста
    public void createPost(PostCreateRequest request) {
        // Проверка на наличие запрещённых слов
        if (badWordChecker.containsBadWords(request.getContent())) {
            throw new RuntimeException("Пост содержит запрещённые слова");
        }

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setLikesCount(0);
        post.setCommentsCount(0);

        postRepository.save(post);
    }

    // Метод для получения всех постов
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponse(
                        post.getPostId(),
                        post.getAuthor().getId(),
                        post.getAuthor().getName(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getLikesCount(),
                        post.getCommentsCount()
                ))
                .collect(Collectors.toList());
    }

    // Метод для обновления поста
    public void updatePost(Long postId, PostCreateRequest request) {
        // Проверка на наличие запрещённых слов
        if (badWordChecker.containsBadWords(request.getContent())) {
            throw new RuntimeException("Пост содержит запрещённые слова");
        }

        // Получаем пост из базы
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        // Обновляем содержимое поста
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now()); // Можно обновить дату на текущее время

        // Сохраняем обновлённый пост в базе
        postRepository.save(post);
    }

    // Метод для удаления поста
    public void deletePost(Long postId) {
        // Проверка, существует ли пост
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        // Удаляем пост из базы
        postRepository.delete(post);
    }

    // Метод для увеличения количества лайков на 1
    public void incrementLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        // Увеличиваем количество лайков на 1
        post.setLikesCount(post.getLikesCount() + 1);

        // Сохраняем обновленный пост в базе
        postRepository.save(post);
    }

    // Метод для поиска постов по запросу
    public List<PostResponse> searchPosts(String query) {
        return postRepository.findAll().stream()
                .filter(post ->
                        post.getContent().toLowerCase().contains(query.toLowerCase()) ||
                                post.getAuthor().getName().toLowerCase().contains(query.toLowerCase())
                )
                .map(post -> new PostResponse(
                        post.getPostId(),
                        post.getAuthor().getId(),
                        post.getAuthor().getName(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getLikesCount(),
                        post.getCommentsCount()
                ))
                .collect(Collectors.toList());
    }
}
