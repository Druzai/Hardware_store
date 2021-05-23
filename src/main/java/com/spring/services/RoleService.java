package com.spring.services;

import com.spring.models.Role;
import com.spring.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Служба для работы с ролями.
 */
@Service
public class RoleService {
    /**
     * Репозиторий для взаимодействия с таблицей "role".
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Получение списка всех ролей.
     *
     * @return список ролей
     */
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
