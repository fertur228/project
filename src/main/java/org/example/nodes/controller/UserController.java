package org.example.nodes.controller;

import org.example.nodes.dto.BioUpdateRequest;
import org.example.nodes.dto.UserDTO;
import org.example.nodes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")   // если фронт и бэк раздельно
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* ---------- Пользователь целиком ---------- */

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/search?query=...
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam("query") String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

    /* ---------- Работа с аватаром ---------- */

    // POST /api/users/{id}/avatar
    @PostMapping("/{id}/avatar")
    public ResponseEntity<UserDTO> uploadAvatar(@PathVariable Long id,
                                                @RequestParam("file") MultipartFile file) {
        try {
            UserDTO userDTO = userService.uploadAvatar(id, file);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /* ---------- Работа с биографией ---------- */

    // GET /api/users/{id}/bio   — получить только биографию
    @GetMapping("/{id}/bio")
    public ResponseEntity<?> getUserBio(@PathVariable Long id) {
        return userService.getBio(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/users/{id}/bio  — создать биографию (201 CREATED)
    @PostMapping("/{id}/bio")
    public ResponseEntity<UserDTO> createUserBio(@PathVariable Long id,
                                                 @RequestBody BioUpdateRequest request) {
        UserDTO dto = userService.updateBio(id, request.getBio());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // PATCH /api/users/{id}/bio — частичное обновление
    @PatchMapping("/{id}/bio")
    public ResponseEntity<UserDTO> patchUserBio(@PathVariable Long id,
                                                @RequestBody BioUpdateRequest request) {
        return ResponseEntity.ok(userService.updateBio(id, request.getBio()));
    }

    // PUT /api/users/{id}/bio   — полная замена/создание, идемпотентно
    @PutMapping("/{id}/bio")
    public ResponseEntity<UserDTO> putUserBio(@PathVariable Long id,
                                              @RequestBody BioUpdateRequest request) {
        return ResponseEntity.ok(userService.updateBio(id, request.getBio()));
    }
}
