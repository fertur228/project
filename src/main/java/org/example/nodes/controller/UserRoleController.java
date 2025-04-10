package org.example.nodes.controller;

import org.example.nodes.model.Role;
import org.example.nodes.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRoleController {

    private final UserService userService;

    // Конструктор для инъекции зависимости
    public UserRoleController(UserService userService) {
        this.userService = userService;
    }

    // Эндпоинт для изменения роли пользователя
    @PutMapping("/{userId}/role")
    public ResponseEntity<String> changeUserRole(@PathVariable Long userId, @RequestParam Role newRole) {
        try {
            userService.changeUserRole(userId, newRole);
            return ResponseEntity.ok("Роль пользователя успешно изменена");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Ошибка: " + e.getMessage());
        }
    }
}
