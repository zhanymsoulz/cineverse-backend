package com.unime.CineVerse.controller;

import com.unime.CineVerse.DTO.ReviewDTO;
import com.unime.CineVerse.DTO.ReviewResponseDTO;
import com.unime.CineVerse.model.Review;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.service.ReviewService;
import com.unime.CineVerse.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewDTO dto, HttpServletRequest request) {
        String username = userService.extractUsernameFromRequest(request);
        Users user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new ReviewResponseDTO(reviewService.createReview(user, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(new ReviewResponseDTO(reviewService.getReview(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long id, @RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(new ReviewResponseDTO(reviewService.updateReview(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/movie/{movieId}")
public ResponseEntity<List<Review>> getReviewsByMovie(@PathVariable Long movieId) {
    return ResponseEntity.ok(reviewService.getReviewsByMovie(movieId));
}
}

