// UserController.java
package org.example.nodes.controller;

import org.example.nodes.model.User;
import org.example.nodes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // если фронт и бэк раздельно
public class UserController {

    private final UserService userService;

    @Autowired // или через конструктор
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }
}
