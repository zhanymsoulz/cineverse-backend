package com.unime.CineVerse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/greet")
    public String greet(){
        return "Hello to SE";
    }
}
