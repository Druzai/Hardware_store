package com.app.repositories;

import com.app.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с таблицей "products".
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT u FROM Product u WHERE :searchQuery LIKE LOWER(u.name) OR :searchQuery LIKE LOWER(u.brand)" +
            " OR :searchQuery LIKE LOWER(u.category) OR :searchQuery LIKE LOWER(u.vendorCode)")
    List<Product> search(String searchQuery);
}
