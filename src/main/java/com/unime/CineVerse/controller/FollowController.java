package com.unime.CineVerse.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.service.FollowService;
import com.unime.CineVerse.service.JWTService;
import com.unime.CineVerse.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired
    private JWTService jwtService;
    // This class will handle the HTTP requests related to following functionality
    // It will interact with the FollowService to perform operations like follow, unfollow, and get followers/following lists
    
    @Autowired
    private UserService userService;
    private String extractUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return jwtService.extractUserName(token);
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<?> followUser(@PathVariable Long id, HttpServletRequest request) {
        Long userId = userService.getUserByUsername(extractUsernameFromRequest(request)).getId();
        if (userId == null) {
            return new ResponseEntity<>("Unauthorized or invalid token", HttpStatus.UNAUTHORIZED);
        }
        followService.followUser(userId, id);
        return new ResponseEntity<>("User followed successfully", HttpStatus.OK);
    }
  
    @DeleteMapping("/{id}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id, HttpServletRequest request) {
        String username = extractUsernameFromRequest(request);
        Users user = userService.getUserByUsername(username);
        if (user == null || user.getId() == null) {
            return new ResponseEntity<>("Unauthorized or invalid token", HttpStatus.UNAUTHORIZED);
        }
        followService.unfollowUser(user.getId(), id);
        return new ResponseEntity<>("User unfollowed successfully", HttpStatus.OK);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }  

    @GetMapping("/following/{userId}")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }

    @GetMapping("/followers/{userId}/count")
    public ResponseEntity<Long> countFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.countFollowers(userId));
    }

    @GetMapping("/following/{userId}/count")
    public ResponseEntity<Long> countFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.countFollowing(userId));
    }
}
