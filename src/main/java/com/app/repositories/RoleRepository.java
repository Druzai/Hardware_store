package com.app.repositories;

import com.app.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с таблицей "role".
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
