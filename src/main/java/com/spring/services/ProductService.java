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

/**
 * Служба для работы с продуктами.
 */
@Service
@Slf4j
public class ProductService {
    /**
     * Репозиторий для взаимодействия с таблицей "products".
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Получение списка всех продуктов.
     *
     * @return список продуктов
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Получение списка списков всех продуктов для отображения в виде таблицы.
     *
     * @return список списков всех продуктов
     */
    @Transactional(readOnly = true)
    public List<List<Product>> getAllSortedProducts() {
        return sortProducts(productRepository.findAll());
    }

    /**
     * Получение списка списков продуктов по запросу для отображения в виде таблицы.
     *
     * @param searchQuery строка запроса
     * @return список списков найденых продуктов
     */
    @Transactional(readOnly = true)
    public List<List<Product>> searchProductsSorted(String searchQuery) {
        return sortProducts(productRepository.search(searchQuery.toLowerCase()));
    }

    /**
     * Получение продукта.
     *
     * @param vendorId артикул продукта
     * @return продукт
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProduct(int vendorId) {
        var getProduct = productRepository.findById(vendorId);
        if (getProduct.isPresent())
            log.info("Get existing order");
        else
            log.info("Didn't get nonexistent order");
        return getProduct;
    }

    /**
     * Добавление продукта.
     *
     * @param newProduct класс продукта
     * @return класс добавленного продукта
     */
    @Transactional
    public Product addProduct(Product newProduct) {
        log.info("Add new product");
        productRepository.save(newProduct);
        return newProduct;
    }

    /**
     * Обновление продукта.
     *
     * @param vendorId   артикул продукта
     * @param newProduct класс продукта
     * @return класс обновлённого продукта или исключение, если продукт с таким артикулом не найден
     */
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

    /**
     * Удаление продукта.
     *
     * @param vendorId артикул продукта
     * @return товар удалён или нет
     */
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

    /**
     * Преобразование списка продуктоа в список списка продуктов
     *
     * @param oldList список продуктов
     * @return список списка продуктов
     */
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

    /**
     * Преобразование кортежа продуктоа в список списка продуктов
     *
     * @param oldSet кортеж продуктов
     * @return список списка продуктов
     */
    public List<List<Product>> sortProducts(Set<Product> oldSet) {
        return sortProducts(new ArrayList<>(oldSet));
    }
}
