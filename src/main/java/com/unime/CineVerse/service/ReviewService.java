package com.unime.CineVerse.service;

import com.unime.CineVerse.DTO.ReviewDTO;
import com.unime.CineVerse.model.Review;
import com.unime.CineVerse.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(ReviewDTO dto) {
        Review review = new Review();
        review.setUserId(dto.getUserId());
        review.setMovieId(dto.getMovieId());
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        return reviewRepository.save(review);
    }

    public Review getReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public Review updateReview(Long id, ReviewDTO dto) {
        Review review = getReview(id);
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}

