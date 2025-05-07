package org.example.nodes.dto;

import org.example.nodes.model.Role;

public class UserDTO {

    private Long   id;
    private String name;
    private String email;
    private Role   role;
    private String avatarPath;   // путь к аватару
    private String bio;          // ✨ новая биография

    /** Пустой конструктор нужен для (де)сериализации */
    public UserDTO() {}

    /** Полный конструктор */
    public UserDTO(Long id,
                   String name,
                   String email,
                   Role role,
                   String avatarPath,
                   String bio) {
        this.id         = id;
        this.name       = name;
        this.email      = email;
        this.role       = role;
        this.avatarPath = avatarPath;
        this.bio        = bio;
    }

    /* ---------- геттеры и сеттеры ---------- */
    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public String getName()           { return name; }
    public void setName(String name)  { this.name = name; }

    public String getEmail()          { return email; }
    public void setEmail(String email){ this.email = email; }

    public Role getRole()             { return role; }
    public void setRole(Role role)    { this.role = role; }

    public String getAvatarPath()     { return avatarPath; }
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getBio()            { return bio; }
    public void setBio(String bio)    { this.bio = bio; }
}
