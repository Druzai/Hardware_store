package com.spring.controllers;

import com.spring.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String getIndex(Model model) {
        var products = productService.getAllProducts();
        products = products.subList(0, Math.min(products.size(), 10));
        if (products.size() > 1) {
            model.addAttribute("firstProduct", products.get(0));
            model.addAttribute("products", products.subList(1, products.size()));
        } else
            model.addAttribute("firstProduct", null);
        return "index";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String search, Model model) {
        model.addAttribute("products", productService.searchProductsSorted(search));
        return "search";
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}
