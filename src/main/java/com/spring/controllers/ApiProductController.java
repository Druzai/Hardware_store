package com.spring.controllers;

import com.spring.models.Product;
import com.spring.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


/**
 * Контроллер для добавления/изменения/удаления продуктов запросами через api.
 */
@RestController
@RequestMapping(path = "/api/products", produces = "application/json")
public class ApiProductController {
    /**
     * Служба для работы с продуктами.
     */
    @Autowired
    private ProductService productService;

    /**
     * Получение списка всех продуктов.
     *
     * @return список продуктов
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Получение продукта по его артикула.
     *
     * @param vendorId актикул продукта
     * @return продукт или подмиется исключение, если его не удалось найти
     */
    @GetMapping("/find/{id}")
    public Product getProduct(@PathVariable("id") int vendorId) {
        return productService.getProduct(vendorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Добавление продукта.
     *
     * @param name                название продукта
     * @param description         описание продукта
     * @param bytes               массив байтов, представляющий картинку
     * @param brand               брэнд продукта
     * @param material            материал продукта
     * @param manufacturerCountry страна изготовления продукта
     * @param category            категория продукта
     * @param price               цена продукта
     * @param weight              вес продукта
     * @return сохранённый продукт или подмиется исключение, если его не удалось найти
     */
    @GetMapping(path = "/add")
    public Product addProduct(@RequestParam String name, @RequestParam String description, @RequestParam(required = false) byte[] bytes,
                              @RequestParam String brand, @RequestParam String material, @RequestParam String manufacturerCountry,
                              @RequestParam String category, @RequestParam double price, @RequestParam double weight) {
        if (!name.isEmpty() && !description.isEmpty() && !material.isEmpty() && !brand.isEmpty() &&
                !manufacturerCountry.isEmpty() && !category.isEmpty() && price >= 0 && weight > 0)
            return productService.addProduct(new Product(name, description, bytes, brand, material,
                    manufacturerCountry, category, price, weight));
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    /**
     * Обновление существующего продукта по его артикулу.
     *
     * @param vendorId            артикул продукта
     * @param name                название продукта
     * @param description         описание продукта
     * @param bytes               массив байтов, представляющий картинку
     * @param brand               брэнд продукта
     * @param material            материал продукта
     * @param manufacturerCountry страна изготовления продукта
     * @param category            категория продукта
     * @param price               цена продукта
     * @param weight              вес продукта
     * @return сохранённый продукт или подмиется исключение, если его не удалось найти
     */
    @PostMapping(path = "/update/{id}")
    public Product updateProduct(@PathVariable("id") int vendorId, @RequestParam String name,
                                 @RequestParam String description, @RequestParam(required = false) byte[] bytes, @RequestParam String brand,
                                 @RequestParam String material, @RequestParam String manufacturerCountry,
                                 @RequestParam String category, @RequestParam double price, @RequestParam float weight) {
        if (!name.isEmpty() && !description.isEmpty() && !material.isEmpty() && !brand.isEmpty() &&
                !manufacturerCountry.isEmpty() && !category.isEmpty() && price >= 0 && weight > 0 && vendorId > 0)
            return productService.updateProduct(vendorId,
                    new Product(name, description, bytes, brand, material, manufacturerCountry, category, price, weight));
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    /**
     * Удаление продукта по его артикулу.
     *
     * @param vendorId артикул продукта
     * @return строчка об успешном удалении продукта или подмиется исключение, если его не удалось найти
     */
    @DeleteMapping("/del/{id}")
    public String deleteProduct(@PathVariable("id") int vendorId) {
        if (productService.deleteProduct(vendorId))
            return "Deleted";
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключений RuntimeException.
     *
     * @param ex исключение
     * @return HTTP 404 ошибка
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}