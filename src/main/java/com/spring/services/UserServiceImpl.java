package com.spring.services;

import com.spring.models.Role;
import com.spring.models.User;
//import com.spring.repositories.RoleRepository;
import com.spring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(new Role(2L, "ROLE_USER")));
//        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
