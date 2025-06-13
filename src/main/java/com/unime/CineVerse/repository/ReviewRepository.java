package com.unime.CineVerse.repository;

import com.unime.CineVerse.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}