package de.oguz.buglab.controller;

import de.oguz.buglab.model.Product;
import de.oguz.buglab.service.BugToggleService;
import de.oguz.buglab.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final BugToggleService bugToggleService;

    public ProductController(ProductService productService,
                             BugToggleService bugToggleService) {
        this.productService = productService;
        this.bugToggleService = bugToggleService;
    }

    @GetMapping("/products")
    public String products(@RequestParam(name = "q", required = false) String query,
                           @RequestParam(name = "category", required = false) String category,
                           @RequestParam(name = "reset", required = false) String reset,
                           @RequestParam(name = "fromCart", required = false) String fromCart,
                           Model model) {

        if (bugToggleService.isEnabled("api-003") && "true".equalsIgnoreCase(reset)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "BUG-API-003: Reset request failed"
            );
        }

        if (bugToggleService.isEnabled("api-004") && "true".equalsIgnoreCase(fromCart)) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "BUG-API-004: Continue shopping from cart failed"
            );
        }

        if (bugToggleService.isEnabled("api-005")
                && category != null
                && category.equalsIgnoreCase("Adapter & Hubs")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "BUG-API-005: Category request failed"
            );
        }

        List<Product> products = productService.search(query, category)
                .stream()
                .map(this::applyProductListBugs)
                .toList();

        int resultCount = products.size();

        if (bugToggleService.isEnabled("ui-004")
                && category != null
                && category.equalsIgnoreCase("Audio")) {
            resultCount = resultCount + 3;
        }

        String selectedCategory = category == null ? "" : category;

        if (bugToggleService.isEnabled("ui-005")
                && category != null
                && category.equalsIgnoreCase("Monitore")) {
            selectedCategory = "Audio";
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.findCategories());
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("selectedCategory", selectedCategory);
        model.addAttribute("resultCount", resultCount);
        model.addAttribute("totalProductCount", productService.findAll().size());

        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (bugToggleService.isEnabled("api-002") && id.equals(4L)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "BUG-API-002: Laptop Stand detail page not found"
            );
        }

        try {
            Product product = productService.findById(id);
            product = applyProductDetailBugs(product);
            model.addAttribute("product", product);
            return "product-detail";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Produkt wurde nicht gefunden.");
            return "redirect:/products";
        }
    }

    private Product applyProductListBugs(Product product) {
        if (bugToggleService.isEnabled("ui-002") && product.id().equals(1L)) {
            return copyProduct(
                    product,
                    new BigDecimal("21.99"),
                    product.available(),
                    product.rating(),
                    product.badge(),
                    product.stock(),
                    product.deliveryInfo()
            );
        }

        if (bugToggleService.isEnabled("ui-006") && product.id().equals(8L)) {
            return copyProduct(
                    product,
                    product.price(),
                    product.available(),
                    product.rating(),
                    product.badge(),
                    product.stock(),
                    "Lieferung in 5-7 Werktagen"
            );
        }

        if (bugToggleService.isEnabled("ui-007") && product.id().equals(9L)) {
            return copyProduct(
                    product,
                    product.price(),
                    product.available(),
                    product.rating(),
                    "Ausverkauft",
                    product.stock(),
                    product.deliveryInfo()
            );
        }

        return product;
    }

    private Product applyProductDetailBugs(Product product) {
        if (bugToggleService.isEnabled("ui-003") && product.id().equals(1L)) {
            return copyProduct(
                    product,
                    product.price(),
                    product.available(),
                    3.9,
                    product.badge(),
                    product.stock(),
                    product.deliveryInfo()
            );
        }

        return product;
    }

    private Product copyProduct(Product product,
                                BigDecimal price,
                                boolean available,
                                double rating,
                                String badge,
                                int stock,
                                String deliveryInfo) {
        return new Product(
                product.id(),
                product.name(),
                product.description(),
                price,
                available,
                product.category(),
                product.icon(),
                rating,
                product.reviewCount(),
                badge,
                stock,
                deliveryInfo
        );
    }
}