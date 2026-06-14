package de.oguz.buglab.controller;

import de.oguz.buglab.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestInfoController {

    private final ProductService productService;

    public TestInfoController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/test-info")
    public String testInfo(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", productService.findCategories());
        model.addAttribute("totalProductCount", productService.findAll().size());
        return "test-info";
    }
}