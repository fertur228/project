package org.example.nodes.controller;

import org.example.nodes.dto.BannedWordRequest;
import org.example.nodes.dto.BannedWordResponse;
import org.example.nodes.model.BannedWord;
import org.example.nodes.service.BannedWordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/banned-words")
public class BannedWordController {

    private final BannedWordService bannedWordService;

    public BannedWordController(BannedWordService bannedWordService) {
        this.bannedWordService = bannedWordService;
    }

    /* ───────────── POST: создать слово ───────────── */
    @PostMapping
    public ResponseEntity<BannedWordResponse> addBannedWord(
            @RequestBody BannedWordRequest request) {

        if (request.getWord() == null || request.getWord().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // addBannedWord теперь возвращает сохранённую сущность
        BannedWord saved = bannedWordService.addBannedWord(request.getWord());
        BannedWordResponse body = toDto(saved);

        return ResponseEntity
                .created(URI.create("/api/banned-words/" + body.getId()))
                .body(body);                    // ← JSON: { "id": ..., "word": "..." }
    }

    /* ───────────── DELETE: удалить ───────────── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBannedWord(@PathVariable Long id) {
        bannedWordService.removeBannedWord(id);
        return ResponseEntity.noContent().build(); // 204 без тела
    }

    /* ───────────── GET: список ───────────── */
    @GetMapping
    public ResponseEntity<List<BannedWordResponse>> getAllBannedWords() {
        List<BannedWordResponse> response = bannedWordService.getActiveBannedWords()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    /* ───────────── mapper ───────────── */
    private BannedWordResponse toDto(BannedWord e) {
        return new BannedWordResponse(e.getId(), e.getWord());
    }
}
