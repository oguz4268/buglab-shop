package de.oguz.buglab.controller;

import de.oguz.buglab.model.Cart;
import de.oguz.buglab.model.CheckoutForm;
import de.oguz.buglab.service.BugToggleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class CheckoutController {

    private static final String CART_SESSION_KEY = "cart";

    private final BugToggleService bugToggleService;

    public CheckoutController(BugToggleService bugToggleService) {
        this.bugToggleService = bugToggleService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        Cart cart = getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (!model.containsAttribute("checkoutForm")) {
            model.addAttribute("checkoutForm", new CheckoutForm());
        }

        model.addAttribute("cart", cart);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String submitCheckout(@Valid CheckoutForm checkoutForm,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Cart cart = getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (bugToggleService.isEnabled("api-006")
                && checkoutForm.getPaymentMethod() != null
                && isCreditCardPayment(checkoutForm.getPaymentMethod())) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "BUG-API-006: Credit card checkout failed"
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cart);
            return "checkout";
        }

        String orderNumber = generateOrderNumber();

        BigDecimal orderTotal = cart.getSubtotal();

        if (bugToggleService.isEnabled("ui-010")) {
            orderTotal = orderTotal.add(new BigDecimal("7.50"));
        }

        redirectAttributes.addFlashAttribute("orderNumber", orderNumber);
        redirectAttributes.addFlashAttribute("customerName", checkoutForm.getFullName());
        redirectAttributes.addFlashAttribute("customerEmail", checkoutForm.getEmail());
        redirectAttributes.addFlashAttribute("orderTotal", orderTotal);
        redirectAttributes.addFlashAttribute("itemCount", cart.getRealTotalQuantity());

        cart.clear();

        return "redirect:/order-confirmation";
    }

    @GetMapping("/order-confirmation")
    public String orderConfirmation() {
        return "order-confirmation";
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

        cart.setCountPositionsInsteadOfQuantity(bugToggleService.isEnabled("ui-009"));

        return cart;
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return "BL-" + timestamp;
    }

    private boolean isCreditCardPayment(String paymentMethod) {
        String normalizedPaymentMethod = paymentMethod.trim().toLowerCase();

        return normalizedPaymentMethod.equals("credit-card")
                || normalizedPaymentMethod.equals("credit_card")
                || normalizedPaymentMethod.equals("creditcard")
                || normalizedPaymentMethod.equals("kreditkarte")
                || normalizedPaymentMethod.equals("card");
    }
}