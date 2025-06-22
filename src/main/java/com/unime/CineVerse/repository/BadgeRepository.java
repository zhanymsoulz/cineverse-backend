package com.unime.CineVerse.repository;

import com.unime.CineVerse.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByName(String name);
}