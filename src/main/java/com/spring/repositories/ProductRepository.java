package com.spring.repositories;

import com.spring.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT u FROM Product u WHERE u.name = :name")
    Iterable<Product> findByName(String name);

    @Query(value = "SELECT u FROM Product u WHERE u.vendorCode in :ids")
    Iterable<Product> findByIds(List<Integer> ids);

    @Query(value = "SELECT u FROM Product u WHERE :searchQuery LIKE LOWER(u.name) OR :searchQuery LIKE LOWER(u.brand)" +
            " OR :searchQuery LIKE LOWER(u.category) OR :searchQuery LIKE LOWER(u.vendorCode)")
    List<Product> search(String searchQuery);
}
