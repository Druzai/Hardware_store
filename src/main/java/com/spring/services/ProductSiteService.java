package com.spring.services;

import com.spring.models.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductSiteService {
    AtomicInteger maxorderId = new AtomicInteger(0);
    List<Product> products = new ArrayList<>();

    public List<Product> getAllProducts() {
        return products;
    }

    public Optional<Product> getProduct(int vendorId) {
        if (products.stream().anyMatch(x -> x.getVendorCode() == vendorId)) {
            log.info("Get existing order");
            return Optional.of(products.stream().filter(x -> x.getVendorCode() == vendorId).findFirst().get());
        } else {
            log.info("Didn't get nonexistent order");
            return Optional.empty();
        }
    }

    public Product addProduct(Product newProduct) {
        log.info("Add new product");
        newProduct.setVendorCode(this.maxorderId.incrementAndGet());
        this.products.add(newProduct);
        return newProduct;
    }

    public Product updateProduct(int vendorId, Product newProduct) {
        var oldProduct = getProduct(vendorId);
        if (oldProduct.isEmpty())
            throw new IllegalArgumentException("Product wasn't found!");
        else {
            log.info("Update existing order");
            newProduct.setVendorCode(oldProduct.get().getVendorCode());
            this.products.remove(oldProduct.get());
            this.products.add(newProduct);
            return newProduct;
        }
    }

    public boolean deleteProduct(int vendorId) {
        if (getProduct(vendorId).isPresent()) {
            log.info("Delete existing order");
            products.remove(getProduct(vendorId).get());
            return true;
        } else {
            log.info("Didn't delete nonexistent order");
            return false;
        }
    }
}
