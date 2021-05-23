package com.spring.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Модель пользователя.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    /**
     * Id пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Логин пользователя.
     */
    private String username;
    /**
     * Зашифрованный пароль пользователя.
     */
    private String password;

    /**
     * Пароль пользователя, для подтверждения при прохождении регистрации.
     */
    @Transient
    private String passwordConfirm;

    /**
     * Кортеж ролей пользователя.
     */
    @ManyToMany
    private Set<Role> roles;

    /**
     * Кортеж продуктов в корзине пользователя.
     */
    @ManyToMany
    private Set<Product> products;
}
