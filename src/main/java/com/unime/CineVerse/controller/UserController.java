package com.unime.CineVerse.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.service.JWTService;
import com.unime.CineVerse.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return service.register(user);

    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {

        return service.verify(user);
    }

    @GetMapping("/users/{userId}")
    public Users getUserById(@PathVariable int userId){
        return service.getUserById(userId);
    }
    @GetMapping("/users/me")
public ResponseEntity<Users> getCurrentUser(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractUserName(token); // or extractEmail()

    Users user = userService.getUserById(3);
    return ResponseEntity.ok(user);
}

}
