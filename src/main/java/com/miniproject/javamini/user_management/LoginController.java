package com.miniproject.javamini.user_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public String loginUser(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return "Welcome, " + user.getUsername() + "! Your home address is: " + user.getHomeAddress() + ", Pincode: " + user.getPincode();
        }
        return "User not found!";
    }
}
