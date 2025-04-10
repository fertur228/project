package org.example.nodes.repository;

import org.example.nodes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Поиск по email
    boolean existsByEmail(String email);  // Проверка на существование по email
    Optional<User> findById(Long id);  // Поиск по id
}
