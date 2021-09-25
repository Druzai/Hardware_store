package com.app.services;

import com.app.ProgramTests;
import com.app.models.User;
import com.app.repositories.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserTest extends ProgramTests {
    @Mock
    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> captor;

    @Autowired
    public void setUserAuthService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Before("")
    public void setUp() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setId(1L);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    }

    @Test
    public void UserShouldBeRegistered() {
        String encodedPassword = passwordEncoder.encode("qwerty");
        User newUser = new User();
        newUser.setUsername("test");
        newUser.setPassword(encodedPassword);
        newUser.setId(2L);

        when(userRepository.save(any(User.class))).thenReturn(newUser);
        User user = userRepository.save(newUser);

        assertThat(user).isNotNull();
        verify(userRepository).save(captor.capture());
        User captured = captor.getValue();
        Assertions.assertEquals(newUser.getUsername(), captured.getUsername());
    }

    @Test
    public void UserShouldBeFound() {
        String username = "test";

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Assertions.assertEquals(username, userDetails.getUsername());
    }
}
