package com.example.scheduling.system.controller;

import com.example.scheduling.system.entity.JwtUtil;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        String response = authController.registerUser(user);

        assertEquals("User already exists", response);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testRegisterUser_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        String response = authController.registerUser(user);

        assertEquals("User registered successfully", response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        AuthController.LoginResponse response = authController.loginUser(user);

        assertEquals("Invalid credentials", response.getMessage());
        assertEquals(null, response.getToken());
    }

    @Test
    public void testLoginUser_Success() {
        User existingUser = new User();
        existingUser.setUsername("testUser");
        existingUser.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(existingUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testUser")).thenReturn("jwtToken");

        AuthController.LoginResponse response = authController.loginUser(user);

        assertEquals("Login successful", response.getMessage());
        assertEquals("jwtToken", response.getToken());
    }
}
