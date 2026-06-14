package de.oguz.buglab.api;

import de.oguz.buglab.model.Product;
import de.oguz.buglab.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> products(@RequestParam(name = "q", required = false) String query,
                                  @RequestParam(name = "category", required = false) String category) {
        return productService.search(query, category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> productById(@PathVariable Long id) {
        try {
            Product product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(404)
                    .body(new ApiError("PRODUCT_NOT_FOUND", "Produkt wurde nicht gefunden."));
        }
    }
}