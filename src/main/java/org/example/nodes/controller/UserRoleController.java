package org.example.nodes.controller;

import org.example.nodes.dto.RoleChangeRequest;
import org.example.nodes.dto.UserDTO;
import org.example.nodes.model.Role;
import org.example.nodes.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/")  // Путь для админского функционала
public class UserRoleController {

    private final UserService userService;

    // Конструктор для инъекции зависимости
    public UserRoleController(UserService userService) {
        this.userService = userService;
    }

    // Эндпоинт для изменения роли пользователя
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<String> changeUserRole(@PathVariable Long userId, @RequestBody RoleChangeRequest request) {
        try {
            Role newRole = Role.valueOf(request.getRole());
            userService.changeUserRole(userId, newRole);
            return ResponseEntity.ok("Роль пользователя успешно изменена");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Недопустимая роль");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Ошибка: " + e.getMessage());
        }
    }

    // Эндпоинт для получения всех пользователей
    @GetMapping("/users")  // Путь для получения списка всех пользователей
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        // Получаем всех пользователей через сервис
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



}
