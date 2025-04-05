package org.example.nodes.dto;

public class UserResponse {
    private Long id;
    private String name;
    private String role;

    public UserResponse(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // Геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
