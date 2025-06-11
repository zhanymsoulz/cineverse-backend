package com.unime.CineVerse.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/greet")
    public String greet(){
        return "Hello to SE";
    }
}
