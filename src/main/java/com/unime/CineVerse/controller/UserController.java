package com.unime.CineVerse.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.service.JWTService;
import com.unime.CineVerse.service.UserService;

import io.jsonwebtoken.io.IOException;
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
    String username = jwtService.extractUserName(token); // extract from token

    Users user = userService.getUserByUsername(username); // ⚠️ this must exist
    return ResponseEntity.ok(user);
}

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestPart Users product) {

        Users product1 = null;
        try {
            product1 = service.updateUser(id, product);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
        if (product1 != null) {
            return new ResponseEntity<>("updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/users/me")
public ResponseEntity<String> updateCurrentUser(
        HttpServletRequest request,
        @RequestBody Users userUpdateData
) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractUserName(token); // or extractEmail()

    Users currentUser = userService.getUserByUsername(username);

    try {
        Users updated = service.updateUser(currentUser.getId(), userUpdateData);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    } catch (IOException e) {
        return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
    }
}


}
