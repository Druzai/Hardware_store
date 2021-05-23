package com.spring.controllers;

import com.spring.models.Product;
import com.spring.services.ProductService;
import com.spring.services.SecurityService;
import com.spring.services.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Контроллер для добавления/изменения/удаления продуктов на frontend.
 */
@Controller
public class ProductController {
    /**
     * Служба для работы с продуктами.
     */
    @Autowired
    private ProductService productService;

    /**
     * Служба для работы с пользователями.
     */
    @Autowired
    private UserService userService;

    /**
     * Служба для работы с аутентификацией пользователей.
     */
    @Autowired
    private SecurityService securityService;

    /**
     * Получение страницы всех продуктов.
     *
     * @param model модель страницы
     * @return страница "products"
     */
    @GetMapping("/products")
    public String getAllProducts(Model model) {
        setProductsOnModel(model);
        model.addAttribute("products", productService.getAllSortedProducts());
        return "products";
    }

    /**
     * Поиск продуктов удовлетворяющих запросу.
     *
     * @param search строка запроса
     * @param model  модель страницы
     * @return страница "search"
     */
    @GetMapping("/search")
    public String searchProducts(@RequestParam String search, Model model) {
        setProductsOnModel(model);
        model.addAttribute("products", productService.searchProductsSorted(search));
        return "search";
    }

    /**
     * Получение страницы продукта с нужным артикулом.
     *
     * @param id    артикул продукта
     * @param model модель страницы
     * @return страница "currentproduct" или страница "error", если продукта с таким артикулом нет
     */
    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        var product = productService.getProduct(id);
        if (product.isPresent()) {
            if (securityService.isAuthenticated()) {
                model.addAttribute("adminUser",
                        userService.getUser().getRoles().stream().anyMatch(x -> x.getName().equals("ROLE_ADMIN")));
                model.addAttribute("inCart",
                        userService.getUser().getProducts().stream().anyMatch(x -> x.getVendorCode() == product.get().getVendorCode()));
            } else {
                model.addAttribute("adminUser", false);
                model.addAttribute("inCart", false);
            }
            boolean imgExists = product.get().getImageBytes() != null && product.get().getImageBytes().length != 0;
            model.addAttribute("imgExists", imgExists);
            model.addAttribute("needToDelete", imgExists);
            model.addAttribute("product", product.get());
            return "currentproduct";
        } else {
            model.addAttribute("reason", "Не найден товар с артикулом " + id);
            return "error";
        }
    }

    /**
     * Получение страницы добавления нового товара.
     *
     * @param model модель страницы
     * @return страница "addproduct"
     */
    @GetMapping("/product/add")
    public String addProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "addproduct";
    }

    /**
     * Обработка добавления нового товара.
     *
     * @param product класс продукта
     * @return перенаправление на адрес "/products"
     */
    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") Product product) throws IOException {
        if (product.getMultipartFile().getBytes() != null && product.getMultipartFile().getBytes().length != 0)
            product.setImageBytes(product.getMultipartFile().getBytes());
        productService.addProduct(product);
        return "redirect:/products";
    }

    /**
     * Обработка обновления продукта.
     *
     * @param product      класс продукта
     * @param needToDelete необходимость удаления изображения продукта
     * @return перенаправление на адрес "/product/{id}"
     */
    @PostMapping("/product/update")
    public String updateProduct(@ModelAttribute("product") Product product,
                                @RequestParam(value = "needToDelete", required = false) boolean needToDelete) throws IOException {
        if (product.getMultipartFile().getBytes() != null && product.getMultipartFile().getBytes().length != 0
                && !needToDelete)
            product.setImageBytes(product.getMultipartFile().getBytes());
        else if ((product.getMultipartFile().getBytes() != null || product.getMultipartFile().getBytes().length != 0)
                && !needToDelete)
            product.setImageBytes(productService.getProduct(product.getVendorCode()).get().getImageBytes());
        else
            product.setImageBytes(null);
        productService.updateProduct(product.getVendorCode(), product);
        return "redirect:/product/" + product.getVendorCode();
    }

    /**
     * Обработка удаления продукта.
     *
     * @param vendorCode артикул продукта
     * @return перенаправление на адрес "/products"
     */
    @PostMapping("/product/delete")
    public String deleteProduct(@RequestParam int vendorCode) {
        productService.deleteProduct(vendorCode);
        return "redirect:/products";
    }

    /**
     * Получение и отправка изображения продукта.
     *
     * @param id       артикул продукта
     * @param response класс ответа
     */
    @RequestMapping(value = "/product/imageDisplay", method = RequestMethod.GET)
    public void renderImageFromDb(@RequestParam int id, HttpServletResponse response) throws IOException {
        var prod = productService.getProduct(id).get();
        if (prod.getImageBytes() != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(prod.getImageBytes());
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    /**
     * Добавление на модель страницы списка товаров в корзине и кнопок, если пользователь - админ.
     *
     * @param model модель страницы
     */
    private void setProductsOnModel(Model model) {
        if (securityService.isAuthenticated()) {
            model.addAttribute("adminUser",
                    userService.getUser().getRoles().stream().anyMatch(x -> x.getName().equals("ROLE_ADMIN")));
            var listProd = productService.getAllProducts();
            var listUsPr = userService.getUser().getProducts();
            var listExPrCodes = listProd.stream()
                    .filter(listUsPr::contains).map(Product::getVendorCode).collect(Collectors.toList());
            model.addAttribute("listOfExProductCodes", listExPrCodes);
        } else {
            model.addAttribute("adminUser", false);
            model.addAttribute("listOfExProductCodes", new ArrayList<>());
        }
    }
}
