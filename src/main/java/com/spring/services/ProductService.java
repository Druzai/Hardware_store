package com.spring.services;

import com.spring.models.Product;
import com.spring.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<List<Product>> getAllSortedProducts(){
        return this.sortProducts(productRepository.findAll());
    }

    public List<List<Product>> searchProductsSorted(String searchQuery){
        return this.sortProducts(productRepository.search(searchQuery.toLowerCase()));
    }

    public Optional<Product> getProduct(int vendorId) {
        var getProduct = productRepository.findById(vendorId);
        if (getProduct.isPresent())
            log.info("Get existing order");
        else
            log.info("Didn't get nonexistent order");
        return getProduct;
    }

    public Product addProduct(Product newProduct) {
        log.info("Add new product");
        productRepository.save(newProduct);
        return newProduct;
    }

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

    private List<List<Product>> sortProducts(List<Product> oldList){
        int count = 0;
        List<List<Product>> sorted_list = new ArrayList<>();
        while (oldList.size() > count){
            try{
                sorted_list.add(oldList.subList(count, count + 3));
            }
            catch (IndexOutOfBoundsException ex){
                sorted_list.add(oldList.subList(count, oldList.size()));
            }
            count += 3;
        }
        return sorted_list;
    }
}
