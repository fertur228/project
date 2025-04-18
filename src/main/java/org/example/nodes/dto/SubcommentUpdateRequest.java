package org.example.nodes.dto;

public class SubcommentUpdateRequest {
    private String content;  // Новый текст подкомментария

    // Геттеры и сеттеры
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
