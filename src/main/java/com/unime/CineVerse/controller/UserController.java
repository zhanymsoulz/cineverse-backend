package com.unime.CineVerse.controller;


import java.util.HashMap;
import java.util.Map;

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

import com.unime.CineVerse.DTO.UserDTO;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.service.JWTService;
import com.unime.CineVerse.service.UserService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private JWTService jwtService;
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

    @PostMapping("/register")
    public Users register(@RequestBody UserDTO dto) {
        return service.register(dto);

    }

    @PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody Users dto) {
    String token = service.login(dto);
    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    return ResponseEntity.ok(response);
}


    @GetMapping("/{userId}")
    public Users getUserById(@PathVariable Long userId){
        return service.getUserById(userId);
    }
    

    @GetMapping("/me")
public ResponseEntity<Users> getCurrentUser(HttpServletRequest request) {
    String username = extractUsernameFromRequest(request);
    if (username == null) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    Users currentUser = userService.getUserByUsername(username);
    return ResponseEntity.ok(currentUser);
}


    @PutMapping("/{id}")
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
    @PutMapping("/me")
public ResponseEntity<String> updateCurrentUser(
        HttpServletRequest request,
        @RequestBody UserDTO dto
) {
    String username = extractUsernameFromRequest(request);
    if (username == null) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    Users currentUser = userService.getUserByUsername(username);

    try {
        service.updateUser(currentUser.getId(), dto);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
    }
}
@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            service.deleteUserSaga(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}