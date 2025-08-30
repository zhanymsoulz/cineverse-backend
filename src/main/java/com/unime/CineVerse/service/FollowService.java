package com.unime.CineVerse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unime.CineVerse.model.Follow;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.repository.FollowRepository;
import com.unime.CineVerse.repository.UserRepository;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    UserRepository userRepository;
    @Autowired
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
    public void unfollowUser(Long followingUserId, Long followedUserId) {
        Follow follow = followRepository.findByFollowingUserIdAndFollowedUserId(followingUserId, followedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship not found"));
        followRepository.delete(follow);
    }

    public List<Users> getFollowers(Long userId) {
        List<Follow> follows = followRepository.findByFollowedUserId(userId);
        return follows.stream().map(Follow::getFollowingUser).toList();
    }

    public List<Users> getFollowing(Long userId) {
        List<Follow> follows = followRepository.findByFollowingUserId(userId);
        return follows.stream().map(Follow::getFollowedUser).toList();
    }

    public long countFollowers(Long userId) {
        return followRepository.countByFollowedUserId(userId);
    }

    public long countFollowing(Long userId) {
        return followRepository.countByFollowingUserId(userId);
    }
}