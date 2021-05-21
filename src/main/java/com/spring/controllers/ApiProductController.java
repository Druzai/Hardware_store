package com.spring.controllers;

import com.spring.models.Product;
import com.spring.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(path = "/api/products", produces = "application/json")
public class ApiProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/find/{id}")
    public Product getBook(@PathVariable("id") int vendorId) {
        return productService.getProduct(vendorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/add")
    public Product addProduct(@RequestParam String name, @RequestParam String description, @RequestParam String brand,
                           @RequestParam String material, @RequestParam String manufacturerCountry,
                           @RequestParam String category, @RequestParam int price, @RequestParam double weight) {
        if (!name.isEmpty() && !description.isEmpty() && !material.isEmpty() && !brand.isEmpty() &&
                !manufacturerCountry.isEmpty() && !category.isEmpty() && price >= 0 && weight > 0)
            return productService.addProduct(new Product(name, description, brand, material, manufacturerCountry, category, price, weight));
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/update/{id}")
    public Product updateProduct(@PathVariable("id") int vendorId, @RequestParam String name,
                              @RequestParam String description, @RequestParam String brand,
                              @RequestParam String material, @RequestParam String manufacturerCountry,
                              @RequestParam String category, @RequestParam int price, @RequestParam float weight) {
        if (!name.isEmpty() && !description.isEmpty() && !material.isEmpty() && !brand.isEmpty() &&
                !manufacturerCountry.isEmpty() && !category.isEmpty() && price >= 0 && weight > 0 && vendorId > 0)
            return productService.updateProduct(vendorId,
                    new Product(name, description, brand, material, manufacturerCountry, category, price, weight));
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/del/{id}")
    public String deleteProduct(@PathVariable("id") int vendorId) {
        if (productService.deleteProduct(vendorId))
            return "Deleted";
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}