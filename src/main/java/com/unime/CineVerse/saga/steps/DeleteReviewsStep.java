package com.unime.CineVerse.saga.steps;

import java.util.List;

import com.unime.CineVerse.model.Review;
import com.unime.CineVerse.repository.ReviewRepository;
import com.unime.CineVerse.saga.SagaStep;
import com.unime.CineVerse.service.ReviewService;

public class DeleteReviewsStep implements SagaStep {
    private final Long userId;
    private ReviewService reviewService;
    private  ReviewRepository reviewRepository;

    public DeleteReviewsStep(Long userId, ReviewService reviewService, ReviewRepository reviewRepository) {
        this.userId = userId;
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void execute() throws Exception {
        reviewService.deleteAllReviewsByUserId(userId);
        System.out.println("Deleting reviews for user " + userId);
        // Simulate error for testing
        // throw new Exception("Fail deleting reviews");
    }

    @Override
public void compensate() {
    System.out.println("Restoring reviews for user " + userId);
    List<Review> backup = ReviewService.backupStorage.get(userId);
    if (backup != null) {
        reviewRepository.saveAll(backup);
        System.out.println("✅ Restored reviews for user " + userId);
    } else {
        System.out.println("⚠️ No backup found for user " + userId);
    }
}

}
