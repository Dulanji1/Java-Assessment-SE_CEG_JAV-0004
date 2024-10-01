package com.example.scheduling.system.controller;

import com.example.scheduling.system.entity.JwtUtil;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Regular expression to validate Gmail addresses
    private static final String GMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    private static final Pattern GMAIL_PATTERN = Pattern.compile(GMAIL_REGEX);

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Validate Gmail address
        if (!isValidGmail(user.getEmail())) {
            return "Invalid email address. Please use a valid Gmail address.";
        }

        if (userRepository.existsByEmpNo(user.getEmpNo())) {
            return "User already exists under that emp no.";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "User registered successfully";
    }

    private boolean isValidGmail(String email) {
        return email != null && GMAIL_PATTERN.matcher(email).matches();
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            String token = jwtUtil.generateToken(existingUser.get().getUsername());
            return new LoginResponse("Login successful", token);
        }
        return new LoginResponse("Invalid credentials", null);
    }

    @GetMapping("/profile")
    public User getProfile(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7)); // Remove "Bearer " prefix
        return userRepository.findByUsername(username);
    }

    // Delete User by Username
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            return new ResponseEntity<>("User with username: " + username + " not found.", HttpStatus.NOT_FOUND);
        }
        userRepository.delete(existingUser);
        return new ResponseEntity<>("User with username: " + username + " deleted successfully.", HttpStatus.OK);
    }

    // Inner class for login response
    public static class LoginResponse {
        private String message;
        private String token;

        public LoginResponse(String message, String token) {
            this.message = message;
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }
    }
}
