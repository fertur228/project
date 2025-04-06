package org.example.nodes.service;

import org.example.nodes.dto.PostCreateRequest;
import org.example.nodes.dto.PostCreateRequest;
import org.example.nodes.dto.PostResponse;
import org.example.nodes.model.Post;
import org.example.nodes.model.User;
import org.example.nodes.repository.PostRepository;
import org.example.nodes.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void createPost(PostCreateRequest request) {
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

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponse(
                        post.getPostId(),
                        post.getAuthor().getName(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getLikesCount(),
                        post.getCommentsCount()
                ))
                .collect(Collectors.toList());
    }
}
