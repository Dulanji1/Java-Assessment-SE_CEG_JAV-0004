package com.example.scheduling.system.controller;


import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/encode", method = RequestMethod.GET)
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Encode the user's password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String loginUser() {
        // Spring Security handles login, so this can be left blank for now.
        return "Login endpoint";
    }
}
