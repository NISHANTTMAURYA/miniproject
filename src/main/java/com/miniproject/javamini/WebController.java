package com.miniproject.javamini;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello my name is nishant and i am trying to learn something new"); // Pass value to template
        return "index"; // Return the name of the HTML template
    }
}
