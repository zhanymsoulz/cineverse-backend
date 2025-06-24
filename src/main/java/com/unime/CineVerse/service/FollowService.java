package com.unime.CineVerse.service;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.unime.CineVerse.model.Follow;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.repository.FollowRepository;
import com.unime.CineVerse.repository.UserRepository;

@Service
public class FollowService {

    UserRepository userRepository;
    FollowRepository followRepository;
    public void followUser(Long followingUserId, Long followedUserId) {
        if (followingUserId == null || followedUserId == null) {
            throw new IllegalArgumentException("User IDs must not be null.");
        }
        if (followingUserId.equals(followedUserId)) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }

        Users followingUser = userRepository.findById(followingUserId)
                .orElseThrow(() -> new IllegalArgumentException("Following user not found"));
        Users followedUser = userRepository.findById(followedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found"));

        // Optionally, check if the follow relationship already exists
        // if (followRepository.existsByFollowingUserAndFollowedUser(followingUser, followedUser)) {
        //     throw new IllegalStateException("Already following this user.");
        // }

        Follow follow = Follow.builder()
                .followingUser(followingUser)
                .followedUser(followedUser)
                .build();

        followRepository.save(follow);
    }   
}