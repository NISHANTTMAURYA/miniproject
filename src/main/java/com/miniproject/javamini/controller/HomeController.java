package com.miniproject.javamini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToroute() {

        return "redirect:/create-route.html";
    }
}
