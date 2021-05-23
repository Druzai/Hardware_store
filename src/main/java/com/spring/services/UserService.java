package com.spring.services;

import com.spring.models.User;
import com.spring.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Служба для работы с пользователями.
 */
@Service
@Slf4j
public class UserService {
    /**
     * Репозиторий для взаимодействия с таблицей "users".
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Шифровщик паролей с помощью алгоритма bcrypt.
     */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Сохранение пользователя.
     * @param user класс пользователя
     */
    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        log.info("Save new user - " + user.getUsername());
        userRepository.save(user);
    }

    /**
     * Получение пользователя с заданным логином.
     * @param username логин пользователя
     * @return класс пользователя
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        log.info("Find user by username - " + username);
        return userRepository.findByUsername(username);
    }

    /**
     * Получение текущего пользователя.
     * @return класс пользователя
     */
    @Transactional(readOnly = true)
    public User getUser() {
        String username = "";
        try {
            var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        } catch (NullPointerException e) {
            username = "test";
        }
        log.info("Get user by username - " + username);
        return userRepository.findByUsername(username);
    }
}
