package com.spring.controllers;

import com.spring.services.ProductSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class HomeController {
    @Autowired
    private ProductSiteService productSiteService;

    @GetMapping("/")
    public String getIndex(){
        return "index";
    }

    @GetMapping("/products")
    public String getAllProducts(Model model){
        model.addAttribute("products", productSiteService.getAllSortedProducts());
        return "products";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable int id, Model model){
        var product = productSiteService.getProduct(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "currentproduct";
        }
        else
            return "error";

    }

    @GetMapping("/error")
    public String getError(){
        return "error";
    }
}
