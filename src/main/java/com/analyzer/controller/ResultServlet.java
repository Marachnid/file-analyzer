package com.analyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResultServlet {

    @GetMapping("/fileResults")
    public String resultPage() {
        return "fileResults";
    }
}
