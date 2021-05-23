package com.spring.services;

import com.spring.models.Product;
import com.spring.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<List<Product>> getAllSortedProducts() {
        return sortProducts(productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<List<Product>> searchProductsSorted(String searchQuery) {
        return sortProducts(productRepository.search(searchQuery.toLowerCase()));
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProduct(int vendorId) {
        var getProduct = productRepository.findById(vendorId);
        if (getProduct.isPresent())
            log.info("Get existing order");
        else
            log.info("Didn't get nonexistent order");
        return getProduct;
    }

    @Transactional
    public Product addProduct(Product newProduct) {
        log.info("Add new product");
        productRepository.save(newProduct);
        return newProduct;
    }

    @Transactional
    public Product updateProduct(int vendorId, Product newProduct) {
        var oldProduct = getProduct(vendorId);
        if (oldProduct.isEmpty())
            throw new IllegalArgumentException("Product wasn't found!");
        else {
            log.info("Update existing order");
            newProduct.setVendorCode(oldProduct.get().getVendorCode());
            productRepository.save(newProduct);
            return newProduct;
        }
    }

    @Transactional
    public boolean deleteProduct(int vendorId) {
        var currentProduct = getProduct(vendorId);
        if (currentProduct.isPresent()) {
            log.info("Delete existing order");
            productRepository.delete(currentProduct.get());
            return true;
        } else {
            log.info("Didn't delete nonexistent order");
            return false;
        }
    }

    public List<List<Product>> sortProducts(List<Product> oldList) {
        int count = 0;
        List<List<Product>> sorted_list = new ArrayList<>();
        while (oldList.size() > count) {
            try {
                sorted_list.add(oldList.subList(count, count + 3));
            } catch (IndexOutOfBoundsException ex) {
                sorted_list.add(oldList.subList(count, oldList.size()));
            }
            count += 3;
        }
        return sorted_list;
    }

    public List<List<Product>> sortProducts(Set<Product> oldSet) {
        return sortProducts(new ArrayList<>(oldSet));
    }
}
