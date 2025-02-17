package com.analyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/results")
    public String results() {
        return "fileResults";
    }

    @GetMapping("/other")
    public String other() {
        return "other";
    }
}
