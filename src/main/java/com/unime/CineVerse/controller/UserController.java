package com.unime.CineVerse.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.unime.CineVerse.DTO.UserDTO;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.service.JWTService;
import com.unime.CineVerse.service.UserService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody UserDTO dto) {
        return service.register(dto);

    }

    @PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody Users dto) {
    String token = service.verify(dto);
    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    return ResponseEntity.ok(response);
}


    @GetMapping("/users/{userId}")
    public Users getUserById(@PathVariable Long userId){
        return service.getUserById(userId);
    }
    @GetMapping("/users/me")
public ResponseEntity<Users> getCurrentUser(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    System.out.println("Auth header: " + authHeader);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        System.out.println("Missing or invalid Authorization header");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String token = authHeader.substring(7);
    System.out.println("Extracted token: " + token);

    try {
        String username = jwtService.extractUserName(token);
        System.out.println("Extracted username: " + username);

        Users user = userService.getUserByUsername(username);
        if (user == null) {
            System.out.println("User not found in DB");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user);
    } catch (Exception e) {
        System.out.println("Exception occurred: " + e.getMessage());
        e.printStackTrace();  // Optional but helpful
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestPart UserDTO dto) {

        Users user = null;
        try {
            user = service.updateUser(id, dto);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
        if (user != null) {
            return new ResponseEntity<>("updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/users/me")
public ResponseEntity<String> updateCurrentUser(
        HttpServletRequest request,
        @RequestBody UserDTO dto
) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractUserName(token); // or extractEmail()

    Users currentUser = userService.getUserByUsername(username);

    try {
        service.updateUser(currentUser.getId(), dto);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    } catch (IOException e) {
        return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
    }
}
}
