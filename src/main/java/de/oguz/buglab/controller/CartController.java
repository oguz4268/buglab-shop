package de.oguz.buglab.controller;

import de.oguz.buglab.api.BugTriggeredException;
import de.oguz.buglab.model.Cart;
import de.oguz.buglab.model.Product;
import de.oguz.buglab.service.BugToggleService;
import de.oguz.buglab.service.BugTracker;
import de.oguz.buglab.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class CartController {

    private static final String CART_SESSION_KEY = "cart";

    private final ProductService productService;
    private final BugToggleService bugToggleService;
    private final BugTracker bugTracker;

    public CartController(ProductService productService,
                          BugToggleService bugToggleService,
                          BugTracker bugTracker) {
        this.productService = productService;
        this.bugToggleService = bugToggleService;
        this.bugTracker = bugTracker;
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        if (bugToggleService.isEnabled("api-001") && productId.equals(5L)) {
            bugTracker.record("api-001");
            throw new BugTriggeredException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "api-001",
                    "BUG-API-001: Could not add Noise Cancelling Headphones to cart"
            );
        }

        Product product = productService.findById(productId);

        if (!product.available()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Dieses Produkt ist aktuell nicht bestellbar."
            );
            return "redirect:/products";
        }

        if (bugToggleService.isEnabled("ui-008") && product.id().equals(2L)) {
            bugTracker.record("ui-008");
            product = copyProductWithPrice(product, new BigDecimal("79.99"));
        }

        boolean ignoreQuantityInLineTotal =
                bugToggleService.isEnabled("ui-001") && product.id().equals(1L);

        if (ignoreQuantityInLineTotal) {
            bugTracker.record("ui-001");
        }

        Cart cart = getCart(session);
        cart.addProduct(product, ignoreQuantityInLineTotal);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                product.name() + " wurde in den Warenkorb gelegt."
        );

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        if (bugToggleService.isEnabled("api-008") && productId.equals(8L)) {
            bugTracker.record("api-008");
            throw new BugTriggeredException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "api-008",
                    "BUG-API-008: Could not remove Portable SSD 1TB from cart"
            );
        }

        Cart cart = getCart(session);
        cart.removeProduct(productId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Produkt wurde aus dem Warenkorb entfernt."
        );

        return "redirect:/cart";
    }

    private Cart getCart(HttpSession session) {
        Object existingCart = session.getAttribute(CART_SESSION_KEY);

        Cart cart;

        if (existingCart instanceof Cart existing) {
            cart = existing;
        } else {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        boolean countPositions = bugToggleService.isEnabled("ui-009");
        if (countPositions) {
            bugTracker.record("ui-009");
        }
        cart.setCountPositionsInsteadOfQuantity(countPositions);

        return cart;
    }

    private Product copyProductWithPrice(Product product, BigDecimal price) {
        return new Product(
                product.id(),
                product.name(),
                product.description(),
                price,
                product.available(),
                product.category(),
                product.icon(),
                product.rating(),
                product.reviewCount(),
                product.badge(),
                product.stock(),
                product.deliveryInfo()
        );
    }
}