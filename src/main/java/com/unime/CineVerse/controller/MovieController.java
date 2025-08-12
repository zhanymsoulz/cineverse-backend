package com.unime.CineVerse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unime.CineVerse.service.MovieService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/popular")
    public ResponseEntity<String> getPopularMovies() {
        return ResponseEntity.ok(movieService.getPopularMovies());
    }
    @GetMapping("/trending")
    public ResponseEntity<String> getTrendingMovies() {
        return ResponseEntity.ok(movieService.getTrendingMovies());
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }
}

