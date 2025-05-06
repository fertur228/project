package org.example.nodes.dto;

/**
 * Лёгкий объект для вывода запрещённого слова наружу.
 * Содержит только id и само слово.
 */
public class BannedWordResponse {
    private Long id;
    private String word;

    public BannedWordResponse() { }

    public BannedWordResponse(Long id, String word) {
        this.id = id;
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
