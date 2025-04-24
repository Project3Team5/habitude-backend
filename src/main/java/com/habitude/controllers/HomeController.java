package com.habitude.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping("/")
    public String home() {
        return "Welcome Home!";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Secured Home!";
    }
}
