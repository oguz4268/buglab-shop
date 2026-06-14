package de.oguz.buglab.controller;

import de.oguz.buglab.model.Product;
import de.oguz.buglab.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Product> featuredProducts = productService.findAll()
                .stream()
                .filter(Product::available)
                .limit(3)
                .toList();

        model.addAttribute("appName", "TechGear Pro");
        model.addAttribute("subtitle", "Premium Technik und Zubehör für moderne Arbeitsplätze.");
        model.addAttribute("featuredProducts", featuredProducts);

        return "index";
    }
}