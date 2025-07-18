package org.example.nodes.utils;

import org.example.nodes.model.BannedWord;
import org.example.nodes.service.BannedWordService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BadWordChecker {

    private final BannedWordService bannedWordService;

    public BadWordChecker(BannedWordService bannedWordService) {
        this.bannedWordService = bannedWordService;
    }

    public boolean containsBadWords(String content) {
        if (content == null) return false; // avoid NullPointerException

        List<BannedWord> bannedWords = bannedWordService.getActiveBannedWords();
        for (BannedWord word : bannedWords) {
            if (content.toLowerCase().contains(word.getWord().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
