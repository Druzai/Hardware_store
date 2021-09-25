package com.app.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Модель роли.
 */
@Entity
@Table(name = "role")
@Setter
@Getter
public class Role {
    /**
     * Id роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Имя роли.
     */
    private String name;

    /**
     * Кортеж пользователей, у которых данная роль.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    /**
     * Инициализация роли со всеми полями.
     *
     * @param id        id роли
     * @param role_user имя роли
     */
    public Role(long id, String role_user) {
        this.id = id;
        this.name = role_user;
    }

    /**
     * Инициализация пустой роли.
     */
    public Role() {

    }
}
