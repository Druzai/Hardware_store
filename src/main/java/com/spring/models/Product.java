package com.spring.models;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@RequiredArgsConstructor
public class Product {
    private int vendorCode = -1;
    private final String name;
    private final String description;
    private final String brand;
    private final String material;
    private final String manufacturerCountry;
    private final String category;
    private final int price;
    private final float weight;
}
