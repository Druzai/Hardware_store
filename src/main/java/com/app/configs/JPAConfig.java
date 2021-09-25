package com.app.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Класс конфигурации репозиториев.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.app.repositories")
public class JPAConfig {
}
