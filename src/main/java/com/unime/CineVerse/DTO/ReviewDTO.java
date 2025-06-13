package com.unime.CineVerse.DTO;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long userId;
    private Long movieId;
    private String content;
    private Integer rating;
}
