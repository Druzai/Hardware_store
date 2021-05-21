package com.spring.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    private int vendorCode;
//    @Lob
//    private byte[] imageBytes;
    private String name;
    private String description;
    private String brand;
    private String material;
    private String manufacturerCountry;
    private String category;
    private int price;
    private double weight;

    public Product() {
    }

    public Product(String name, String description, String brand, String material, String manufacturerCountry,
                   String category, int price, double weight) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.material = material;
        this.manufacturerCountry = manufacturerCountry;
        this.category = category;
        this.price = price;
        this.weight = weight;
    }
}
