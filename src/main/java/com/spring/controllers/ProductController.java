package com.spring.controllers;

import com.spring.entity.ImageFile;
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

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/products")
    public String getAllProducts(Model model) {
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
        model.addAttribute("products", productService.getAllSortedProducts());
        return "products";
    }

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
            model.addAttribute("img", new ImageFile());
            model.addAttribute("product", product.get());
            return "currentproduct";
        } else {
            model.addAttribute("reason", "Не найден товар с артикулом " + id);
            return "error";
        }
    }

    @GetMapping("/product/add")
    public String addProductPage(Model model) {
        model.addAttribute("img", new ImageFile());
        model.addAttribute("product", new Product());
        return "addproduct";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") Product product,
                             @ModelAttribute("img") ImageFile imageFile) throws IOException {
        if (imageFile.getMultipartFile().getBytes() != null && imageFile.getMultipartFile().getBytes().length != 0)
            product.setImageBytes(imageFile.getMultipartFile().getBytes());
        productService.addProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/product/update")
    public String updateProduct(@ModelAttribute("product") Product product,
                                @ModelAttribute("img") ImageFile imageFile,
                                @RequestParam(value="needToDelete", required=false) boolean needToDelete) throws IOException {
        if (imageFile.getMultipartFile().getBytes() != null && imageFile.getMultipartFile().getBytes().length != 0
                && !needToDelete)
            product.setImageBytes(imageFile.getMultipartFile().getBytes());
        else if ((imageFile.getMultipartFile().getBytes() == null || imageFile.getMultipartFile().getBytes().length == 0)
                && !needToDelete)
            product.setImageBytes(productService.getProduct(product.getVendorCode()).get().getImageBytes());
        else
            product.setImageBytes(null);
        productService.updateProduct(product.getVendorCode(), product);
        return "redirect:/product/" + product.getVendorCode();
    }

    @PostMapping("/product/delete")
    public String deleteProduct(@RequestParam int vendorCode) {
        productService.deleteProduct(vendorCode);
        return "redirect:/products";
    }

    @RequestMapping(value = "/product/imageDisplay", method = RequestMethod.GET)
    public void renderImageFromDb(@RequestParam int id, HttpServletResponse response) throws IOException {
        var prod = productService.getProduct(id).get();
        if (prod.getImageBytes() != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(prod.getImageBytes());
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
