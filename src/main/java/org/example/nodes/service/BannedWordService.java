package org.example.nodes.service;

import org.example.nodes.model.BannedWord;
import org.example.nodes.repository.BannedWordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannedWordService {

    private final BannedWordRepository bannedWordRepository;

    public BannedWordService(BannedWordRepository bannedWordRepository) {
        this.bannedWordRepository = bannedWordRepository;
    }

    /* ───────────── добавить слово ───────────── */
    public BannedWord addBannedWord(String word) {
        BannedWord bannedWord = new BannedWord();
        bannedWord.setWord(word);
        bannedWord.setActive(true);
        return bannedWordRepository.save(bannedWord); // ← возвращаем сохранённую сущность
    }

    /* ───────────── удалить слово ───────────── */
    public void removeBannedWord(Long id) {
        bannedWordRepository.deleteById(id);          // полное удаление
    }

    /* ───────────── получить активные ───────────── */
    public List<BannedWord> getActiveBannedWords() {
        return bannedWordRepository.findByActiveTrue();
    }
}
