package org.example.nodes.controller;

import jakarta.validation.Valid;
import org.example.nodes.dto.UserLoginRequest;
import org.example.nodes.dto.UserRegistrationRequest;
import org.example.nodes.model.User;
import org.example.nodes.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Пользователь зарегистрирован!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request) {
        try {
            User user = userService.authenticateUser(request.getEmail(), request.getPassword());
// hhgf
            // Можно вернуть только нужные поля
            return ResponseEntity.ok().body(Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "role", user.getRole().name()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
