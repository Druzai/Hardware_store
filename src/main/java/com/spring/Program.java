package com.spring;

import com.spring.models.Role;
import com.spring.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Класс - точка входа в приложение.
 */
@SpringBootApplication
public class Program {

    /**
     * Точка входа.
     * @param args аргументы запуска
     */
    public static void main(String[] args) {
        SpringApplication.run(Program.class, args);
    }

    /**
     * Бин с проверкой что в таблице "role" есть роли и добавление этих ролей если нету.
     * @param roleRepository репозиторий связанный с таблицей "role"
     * @return аргументы для передачи в stdout командной строки
     */
    @Bean
    public CommandLineRunner demoData(RoleRepository roleRepository) {
        if (roleRepository.findAll().size() == 0) {
            return args -> roleRepository.saveAll(List.of(new Role(1L, "ROLE_ADMIN"),
                    new Role(2L, "ROLE_USER")));
        }
        return args -> {
        };
    }
}
