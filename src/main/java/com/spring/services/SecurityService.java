package com.spring.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Служба аутентификации пользователя.
 */
@Service
public class SecurityService {
    /**
     * Менеджер аутентификации.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Служба с дополнительной информацией о пользователе для Spring Security.
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Класс логирования.
     */
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    /**
     * Проверка что пользователь аутентифицировался.
     * @return успешный вход или нет
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    /**
     * Автоматический вход в аккаунт.
     * @param username логин пользователя
     * @param password пароль пользователя
     */
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Autologin successful, %s!", username));
        }
    }
}
