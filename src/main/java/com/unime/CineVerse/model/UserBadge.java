package com.unime.CineVerse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // FK column in UserBadge table
    private Users user;

    @ManyToOne
    @JoinColumn(name = "badge_id") // FK column in UserBadge table
    private Badge badge;
    private LocalDateTime awardedAt;

}
