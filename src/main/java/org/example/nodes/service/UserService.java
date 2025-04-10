package org.example.nodes.service;

import org.example.nodes.dto.UserDTO;
import org.example.nodes.dto.UserRegistrationRequest;
import org.example.nodes.model.Role;
import org.example.nodes.model.User;
import org.example.nodes.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Добавляем зависимость от PasswordEncoder

    // Инъекция зависимости через конструктор
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Метод для регистрации пользователя
    public void registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Пользователь уже существует");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        // Используем passwordEncoder для кодирования пароля
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER); // Роль по умолчанию

        userRepository.save(user);
    }

    // Метод для логина
    public User authenticateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Сравниваем пароль, используя passwordEncoder
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return user;
    }

    // Метод для изменения роли пользователя
    public void changeUserRole(Long userId, Role newRole) {
        // Находим пользователя по ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Проверяем, что новая роль является допустимой
        if (newRole == null) {
            throw new RuntimeException("Некорректная роль");
        }

        // Обновляем роль пользователя
        user.setRole(newRole);
        userRepository.save(user);
    }

    // Метод для получения всех пользователей
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll(); // Получаем всех пользователей из репозитория
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole())) // Преобразуем в DTO
                .collect(Collectors.toList());
    }



}
