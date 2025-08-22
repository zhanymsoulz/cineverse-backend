package com.unime.CineVerse.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unime.CineVerse.DTO.UserDTO;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.repository.ReviewRepository;
import com.unime.CineVerse.repository.UserRepository;
import com.unime.CineVerse.saga.SagaExecutor;
import com.unime.CineVerse.saga.steps.DeleteReviewsStep;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;


@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(UserDTO dto) {
        if (existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        Users user= new Users();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        userRepository.save(user);
        return user;
    }

    public String login(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {

            Users existingUser = userRepository.findByUsername(user.getUsername());
            existingUser.setLastActive(LocalDateTime.now());
            userRepository.save(existingUser);

            return jwtService.generateToken(user.getUsername(), user.getId());
        } else {
            return "fail";
        }
    }

    public Users getUserById(Long userId) {
        return userRepository.findById(userId).orElse(new Users());
    }

    public Users getUserByUsername(String username) {
    return userRepository.findByUsername(username);
}

    public Users updateUser(Long id, UserDTO dto) {
    Users existingUser = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
        existingUser.setEmail(dto.getEmail());
    }

    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
        existingUser.setPassword(encoder.encode(dto.getPassword()));
    }

    if (dto.getUsername() != null && !dto.getUsername().isBlank() && !dto.getUsername().equals(existingUser.getUsername())) {
        existingUser.setUsername(dto.getUsername());
    }

    return userRepository.save(existingUser);
}

public void deleteUserSaga(Long userId) {
    SagaExecutor saga = new SagaExecutor();

    // Pass required beans to the step constructors
    saga.addStep(new DeleteReviewsStep(userId, reviewService, reviewRepository));
    // saga.addStep(new DeleteBadgesStep(userId, badgeService, badgeRepository));
    saga.execute();

    try {
        userRepository.deleteById(userId);
    } catch (Exception e) {
        // Optionally compensate if user deletion fails
        // saga.rollback(); // You may need to expose this method
        throw e;
    }
}
public String extractUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return jwtService.extractUserName(token);
    }

public boolean existsByUsername(String username) {
    if(userRepository.findByUsername(username) != null) {
        return true;
    }
    return false;
}



}
