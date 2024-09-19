package com.example.scheduling.system.service;

import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        // Set other user properties as needed
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername("testUser");

        assertEquals("testUser", result.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonExistentUser");
        });

        assertEquals("User not found", exception.getMessage());
    }
}
