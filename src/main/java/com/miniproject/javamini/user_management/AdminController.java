package com.miniproject.javamini.user_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.miniproject.javamini.user_management.UserRepository;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createUser")
    public String createUser(@RequestParam String username, @RequestParam String homeAddress, @RequestParam String pincode) {
        User newUser = new User(username, homeAddress, pincode);
        userRepository.save(newUser);
        return "User created successfully!";
    }
}

