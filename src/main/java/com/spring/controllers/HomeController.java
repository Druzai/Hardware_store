package com.spring.controllers;

import com.spring.services.ProductSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    @Autowired
    private ProductSiteService productSiteService;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productSiteService.getAllSortedProducts());
        return "products";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        var product = productSiteService.getProduct(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "currentproduct";
        } else {
            model.addAttribute("reason", "Не найден товар с арктикулом " + id);
            return "error";
        }
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String search, Model model) {
        model.addAttribute("products", productSiteService.searchProductsSorted(search));
        return "search";
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}
