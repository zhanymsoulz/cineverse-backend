package com.unime.CineVerse.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository repo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return user;
    }

    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }

    public Users getUserById(int userId) {
        return repo.findById(userId).orElse(new Users());
    }

    public Users getUserByUsername(String username) {
    return repo.findByUsername(username);
}

    public Users updateUser(int id, Users userData) {
    Users existingUser = repo.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

    // Update only allowed fields
    existingUser.setEmail(userData.getEmail());
    
    return repo.save(existingUser);
}

}
