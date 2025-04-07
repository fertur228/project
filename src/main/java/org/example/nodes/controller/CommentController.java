package org.example.nodes.controller;

import org.example.nodes.dto.CommentRequest;
import org.example.nodes.dto.CommentUpdateRequest;
import org.example.nodes.model.Comment;
import org.example.nodes.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentRequest request) {
        Comment comment = commentService.addComment(request);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        Comment updatedComment = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(updatedComment);
    }



    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}