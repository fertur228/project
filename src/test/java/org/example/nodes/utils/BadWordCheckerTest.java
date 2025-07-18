package org.example.nodes.utils;

import org.example.nodes.model.BannedWord;
import org.example.nodes.service.BannedWordService;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BadWordCheckerTest {
    @Test
    void handlesNullContent() {
        BannedWordService stub = new BannedWordService(null) {
            @Override
            public List<BannedWord> getActiveBannedWords() {
                return Collections.emptyList();
            }
        };
        BadWordChecker checker = new BadWordChecker(stub);
        assertFalse(checker.containsBadWords(null));
    }
}
