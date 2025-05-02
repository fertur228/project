package org.example.nodes.service;

import org.example.nodes.dto.UserDTO;
import org.example.nodes.dto.UserRegistrationRequest;
import org.example.nodes.model.Role;
import org.example.nodes.model.User;
import org.example.nodes.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Регистрация
    public void registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Пользователь уже существует");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    // Аутентификация
    public User authenticateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return user;
    }

    // Смена роли
    public void changeUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (newRole == null) {
            throw new RuntimeException("Некорректная роль");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }

    // Получение всех пользователей
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Получение по ID
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDTO);
    }

    // Загрузка аватарки и возврат UserDTO
    public UserDTO uploadAvatar(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (file.isEmpty()) {
            throw new RuntimeException("Файл пустой");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Допустимы только изображения");
        }

        String uploadDir = "uploads/avatars/";
        Files.createDirectories(Paths.get(uploadDir));

        String originalName = file.getOriginalFilename();
        String extension = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".jpg";

        String fileName = UUID.randomUUID() + extension;
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE_NEW);

        String relativePath = "/avatars/" + fileName;
        user.setAvatarPath(relativePath);
        userRepository.save(user);

        return mapToDTO(user);
    }

    // Приватный метод преобразования User -> UserDTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getAvatarPath()
        );
    }
    public List<UserDTO> searchUsers(String query) {
        return userRepository.findAll().stream()
                .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase())
                        || user.getEmail().toLowerCase().contains(query.toLowerCase()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

}
