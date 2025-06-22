package com.unime.CineVerse.service;

import com.unime.CineVerse.DTO.ReviewDTO;
import com.unime.CineVerse.event.ReviewPostedEvent;
import com.unime.CineVerse.model.Review;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.repository.ReviewRepository;
import com.unime.CineVerse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    //FOR TEST MOCK DATA
    @Autowired
    private UserRepository userRepository;
    public Review createReview(ReviewDTO dto) {
        Review review = new Review();
        review.setUserId(dto.getUserId());
        review.setMovieId(dto.getMovieId());
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());

        //TEST
        Users user = userRepository.findById(dto.getUserId()).orElseThrow();
        Map<String, Object> stats = new HashMap<>();
        stats.put("review_count", 21);
        stats.put("avg_rating", 4.7);
        stats.put("reputation", user.getReputation());
        stats.put("account_age", 12); // days since account creation or similar

        eventPublisher.publishEvent(new ReviewPostedEvent(this, user, stats));
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

