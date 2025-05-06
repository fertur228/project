package org.example.nodes.repository;

import org.example.nodes.model.BannedWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannedWordRepository extends JpaRepository<BannedWord, Long> {
    List<BannedWord> findByActiveTrue();
}
