package com.unime.CineVerse.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.unime.CineVerse.DTO.FollowDTO;
import com.unime.CineVerse.service.FollowService;
import com.unime.CineVerse.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired
    private JWTService jwtService;
    // This class will handle the HTTP requests related to following functionality
    // It will interact with the FollowService to perform operations like follow, unfollow, and get followers/following lists
    private Long extractUserIdFromRequest(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return null;
    }
    String token = authHeader.substring(7);
    return jwtService.extractUserId(token);
}
    
    @PostMapping("/{id}")
    public ResponseEntity<?> followUser(@RequestBody FollowDTO followDTO, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        if (userId == null) {
            return new ResponseEntity<>("Unauthorized or invalid token", HttpStatus.UNAUTHORIZED);
        }
        followService.followUser(userId, followDTO.getFollowedUserId());
        return new ResponseEntity<>("User followed successfully", HttpStatus.OK);
    }
  
    // @DeleteMapping("/unfollow")
    // public ResponseEntity<?> unfollowUser(@RequestBody FollowDTO followDTO) {
    //     // Logic to unfollow a user
    // }

    // @GetMapping("/followers/{userId}")
    // public ResponseEntity<List<FollowDTO>> getFollowers(@PathVariable Long userId) {
    //     // Logic to get followers of a user
    // }  
}
