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

@Controller
@RequestMapping(path = "/cart")
public class CartController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @GetMapping()
    public String getCart(Model model) {
        var usersProducts = userService.getUser().getProducts();
        var sum = usersProducts.stream().mapToInt(Product::getPrice).sum();
        var usersProductsSorted = productService.sortProducts(usersProducts);
        model.addAttribute("products", usersProductsSorted);
        model.addAttribute("productsSumm", sum);
        return "cart";
    }

    @PostMapping("/add")
    public String addProductToCart(@RequestParam int vendorCode) {
        var user = userService.getUser();
        var userProducts = user.getProducts();
        userProducts.add(productService.getProduct(vendorCode).get());
        user.setProducts(userProducts);
        userService.save(user);
        return "redirect:/cart";
    }

    @PostMapping("/delete")
    public String deleteProductFromCart(@RequestParam int vendorCode) {
        var user = userService.getUser();
        var userProducts = user.getProducts();
        userProducts.remove(productService.getProduct(vendorCode).get());
        user.setProducts(userProducts);
        userService.save(user);
        return "redirect:/cart";
    }

    @PostMapping("/deleteall")
    public String deleteAllProductsFromCart() {
        var user = userService.getUser();
        user.setProducts(new HashSet<>());
        userService.save(user);
        return "redirect:/cart";
    }

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
