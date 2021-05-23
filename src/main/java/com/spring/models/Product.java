package com.spring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Set;

/**
 * Модель продукта.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    /**
     * Артикул продукта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vendorCode;
    /**
     * Массив байтов, представляющий картинку.
     */
    @Lob
    private byte[] imageBytes;
    /**
     * Представление файла картинки в памяти.
     */
    @Transient
    @JsonIgnore
    private MultipartFile multipartFile;
    /**
     * Название продукта.
     */
    private String name;
    /**
     * Описание продукта.
     */
    private String description;
    /**
     * Брэнд продукта.
     */
    private String brand;
    /**
     * Материал продукта.
     */
    private String material;
    /**
     * Страна изготовления продукта.
     */
    private String manufacturerCountry;
    /**
     * Категория продукта.
     */
    private String category;
    /**
     * Цена продукта.
     */
    private double price;
    /**
     * Вес продукта.
     */
    private double weight;

    /**
     * Кортеж пользователей, у которых данный продукт в корзине.
     */
    @ManyToMany(mappedBy = "products")
    private Set<User> users;

    /**
     * Инициализация пустого продукта.
     */
    public Product() {
    }

    /**
     * Инициализация продукта со всеми полями.
     *
     * @param name                название продукта
     * @param description         описание продукта
     * @param imageBytes          массив байтов, представляющий картинку
     * @param brand               брэнд продукта
     * @param material            материал продукта
     * @param manufacturerCountry страна изготовления продукта
     * @param category            категория продукта
     * @param price               цена продукта
     * @param weight              вес продукта
     */
    public Product(String name, String description, byte[] imageBytes, String brand, String material,
                   String manufacturerCountry, String category, double price, double weight) {
        this.name = name;
        this.description = description;
        this.imageBytes = imageBytes;
        this.brand = brand;
        this.material = material;
        this.manufacturerCountry = manufacturerCountry;
        this.category = category;
        this.price = price;
        this.weight = weight;
    }
}
