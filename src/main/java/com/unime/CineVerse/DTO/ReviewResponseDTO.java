package com.unime.CineVerse.DTO;

import com.unime.CineVerse.model.Review;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    public ReviewResponseDTO(Review review) {
        this.id = review.getId();
        this.movieId = review.getMovieId();
        this.content = review.getContent();
        this.rating = review.getRating();
    }
    private Long id;
    private Long movieId;
    private String content;
    private Integer rating;
    
}
