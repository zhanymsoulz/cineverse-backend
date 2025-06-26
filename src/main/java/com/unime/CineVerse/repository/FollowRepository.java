package com.unime.CineVerse.repository;

import com.unime.CineVerse.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>{

    // Method to check if a follow relationship exists between two users
    boolean existsByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);

    // Method to find the count of followers for a user
    long countByFollowedUserId(Long followedUserId);

    // Method to find the count of users followed by a user
    long countByFollowingUserId(Long followingUserId);    
    
    Optional<Follow> findByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);

    List<Follow> findByFollowedUserId(Long followedUserId);

    List<Follow> findByFollowingUserId(Long followingUserId);
}