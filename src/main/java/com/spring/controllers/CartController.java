package com.spring.controllers;

import com.spring.models.Product;
import com.spring.services.ProductService;
import com.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Контроллер для добавления/изменения/удаления продуктов в корзину пользователя.
 */
@Controller
@RequestMapping(path = "/cart")
public class CartController {
    /**
     * Служба для работы с пользователями.
     */
    @Autowired
    private UserService userService;

    /**
     * Служба для работы с продуктами.
     */
    @Autowired
    private ProductService productService;

    /**
     * Получение страницы с корзиной с продуктами пользователя.
     * @param model модель страницы
     * @return страница "cart"
     */
    @GetMapping()
    public String getCart(Model model) {
        var usersProducts = userService.getUser().getProducts();
        var sum = usersProducts.stream().mapToInt(Product::getPrice).sum();
        var usersProductsSorted = productService.sortProducts(usersProducts);
        model.addAttribute("products", usersProductsSorted);
        model.addAttribute("productsSumm", sum);
        return "cart";
    }

    /**
     * Добавление продукта в корзину пользователя.
     * @param vendorCode артикул продукта
     * @return перенаправление на адрес "/cart"
     */
    @PostMapping("/add")
    public String addProductToCart(@RequestParam int vendorCode) {
        var user = userService.getUser();
        var userProducts = user.getProducts();
        userProducts.add(productService.getProduct(vendorCode).get());
        user.setProducts(userProducts);
        userService.save(user);
        return "redirect:/cart";
    }

    /**
     * Удаление продукта из корзины пользователя.
     * @param vendorCode артикул продукта
     * @return перенаправление на адрес "/cart"
     */
    @PostMapping("/delete")
    public String deleteProductFromCart(@RequestParam int vendorCode) {
        var user = userService.getUser();
        var userProducts = user.getProducts();
        userProducts.remove(productService.getProduct(vendorCode).get());
        user.setProducts(userProducts);
        userService.save(user);
        return "redirect:/cart";
    }

    /**
     * Удаление всех продуктов из корзины пользователя.
     * @return перенаправление на адрес "/cart"
     */
    @PostMapping("/deleteall")
    public String deleteAllProductsFromCart() {
        var user = userService.getUser();
        user.setProducts(new HashSet<>());
        userService.save(user);
        return "redirect:/cart";
    }

    /**
     * Обработка заказа выбранных товаров и выдача страницы.
     * @param model модель страницы
     * @return страница "thanksforbuying"
     */
    @PostMapping("/thanksforbuying")
    public String buyProducts(Model model) {
        var user = userService.getUser();
        var productsList = user.getProducts().stream().map(Product::getName).collect(Collectors.joining(", "));
        user.setProducts(new HashSet<>());
        userService.save(user);
        model.addAttribute("userProducts", productsList);
        return "thanksforbuying";
    }
}
