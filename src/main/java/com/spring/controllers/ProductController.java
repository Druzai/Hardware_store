package com.spring.controllers;

import com.spring.models.Product;
import com.spring.services.ProductService;
import com.spring.services.SecurityServiceImpl;
import com.spring.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SecurityServiceImpl securityService;

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        if (securityService.isAuthenticated()) {
            model.addAttribute("adminUser",
                    userService.getUser().getRoles().stream().anyMatch(x -> x.getName().equals("ROLE_ADMIN")));
        } else
            model.addAttribute("adminUser", false);
        model.addAttribute("products", productService.getAllSortedProducts());
        return "products";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        if (securityService.isAuthenticated()) {
            model.addAttribute("adminUser",
                    userService.getUser().getRoles().stream().anyMatch(x -> x.getName().equals("ROLE_ADMIN")));
        } else
            model.addAttribute("adminUser", false);
        var product = productService.getProduct(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "currentproduct";
        } else {
            model.addAttribute("reason", "Не найден товар с арктикулом " + id);
            return "error";
        }
    }

    @GetMapping("/product/add")
    public String addProductPage(Model model){
        model.addAttribute("product", new Product());
        return "addproduct";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") Product product){
        productService.addProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/product/update")
    public String updateProduct(@ModelAttribute("product") Product product){
        productService.updateProduct(product.getVendorCode(), product);
        return "redirect:/product/" + product.getVendorCode();
    }

    @PostMapping("/product/delete")
    public String deleteProduct(@RequestParam int vendorCode){
        productService.deleteProduct(vendorCode);
        return "redirect:/products";
    }
}
