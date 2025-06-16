package com.unime.CineVerse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://api.themoviedb.org/3";

    public String getPopularMovies() {
        String url = BASE_URL + "/movie/popular?api_key=" + apiKey + "&language=en-US&page=1";
        System.out.println(url);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class); // you can parse it to a DTO later
    }
}
