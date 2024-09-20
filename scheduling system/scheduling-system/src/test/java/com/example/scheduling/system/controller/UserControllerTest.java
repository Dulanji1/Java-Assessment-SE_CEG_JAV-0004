package com.example.scheduling.system.controller;

import com.example.scheduling.system.entity.JwtUtil;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setEmpNo("12345");
        user.setPassword("password");

        when(userRepository.existsByEmpNo(user.getEmpNo())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"empNo\": \"12345\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UserExists() throws Exception {
        User user = new User();
        user.setEmpNo("12345");
        user.setPassword("password");

        when(userRepository.existsByEmpNo(user.getEmpNo())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"empNo\": \"12345\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User already exists under that emp no."));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getUsername())).thenReturn("jwtToken");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value("jwtToken"));
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("encodedPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"password\": \"wrongPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invalid credentials"))
                .andExpect(jsonPath("$.token").isEmpty());
    }

    @Test
    public void testGetProfile_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        String token = "Bearer validToken";

        when(jwtUtil.extractUsername("validToken")).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/profile")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("User with username: testuser deleted successfully."));

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/nonexistentuser"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with username: nonexistentuser not found."));
    }
}
