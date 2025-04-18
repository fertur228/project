package org.example.nodes.controller;

import org.example.nodes.dto.SubcommentRequest;
import org.example.nodes.dto.SubcommentUpdateRequest;
import org.example.nodes.model.Subcomment;
import org.example.nodes.service.SubcommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcomments")
public class SubcommentController {

    private final SubcommentService subcommentService;

    public SubcommentController(SubcommentService subcommentService) {
        this.subcommentService = subcommentService;
    }

    @PostMapping
    public ResponseEntity<Subcomment> addSubcomment(@RequestBody SubcommentRequest request) {
        Subcomment subcomment = subcommentService.addSubcomment(request);
        return ResponseEntity.ok(subcomment);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<Subcomment>> getSubcommentsByComment(@PathVariable Long commentId) {
        List<Subcomment> subcomments = subcommentService.getSubcommentsByComment(commentId);
        return ResponseEntity.ok(subcomments);
    }

    @GetMapping("/subcomment/{subcommentId}")
    public ResponseEntity<List<Subcomment>> getSubcommentsBySubcomment(@PathVariable Long subcommentId) {
        List<Subcomment> subcomments = subcommentService.getSubcommentsBySubcomment(subcommentId);
        return ResponseEntity.ok(subcomments);
    }

    @PutMapping("/{subcommentId}")
    public ResponseEntity<Subcomment> updateSubcomment(@PathVariable Long subcommentId, @RequestBody SubcommentUpdateRequest request) {
        Subcomment updatedSubcomment = subcommentService.updateSubcomment(subcommentId, request);
        return ResponseEntity.ok(updatedSubcomment);
    }

    @DeleteMapping("/{subcommentId}")
    public ResponseEntity<Void> deleteSubcomment(@PathVariable Long subcommentId) {
        subcommentService.deleteSubcomment(subcommentId);
        return ResponseEntity.noContent().build();
    }
}
