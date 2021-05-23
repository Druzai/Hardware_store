package com.spring.controllers;

import com.spring.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Контроллер для работы с главной страницей и страницей ошибки 404
 */
@Controller
public class HomeController {
    /**
     * Служба для работы с продуктами
     */
    @Autowired
    private ProductService productService;

    /**
     * Получение главной страницы
     *
     * @param model модель страницы
     * @return страница "index"
     */
    @GetMapping("/")
    public String getIndex(Model model) {
        var products = productService.getAllProducts();
        products = products.subList(0, Math.min(products.size(), 10));
        if (products.size() > 0) {
            model.addAttribute("firstProduct", products.get(0));
            if (products.size() > 1)
                model.addAttribute("products", products.subList(1, products.size()));
            else
                model.addAttribute("products", null);
        } else
            model.addAttribute("firstProduct", null);
        return "index";
    }

    /**
     * Возвращение страницы с ошибкой 404
     *
     * @return страница "error"
     */
    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}
