package org.example.nodes.utils;

import java.util.List;

public class BadWordChecker {
    private static final List<String> BAD_WORDS = List.of("ерасыл", "плохое_слово2", "нецензурное_слово");

    public static boolean containsBadWords(String content) {
        for (String badWord : BAD_WORDS) {
            if (content.toLowerCase().contains(badWord)) {
                return true;
            }
        }
        return false;
    }
}