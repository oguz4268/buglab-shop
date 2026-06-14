package de.oguz.buglab.api;

import de.oguz.buglab.model.CheckoutForm;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutApiController {

    @PostMapping("/validate")
    public CheckoutValidationResponse validateCheckout(@Valid @RequestBody CheckoutForm checkoutForm) {
        return new CheckoutValidationResponse(
                true,
                "Checkout-Daten sind gültig.",
                checkoutForm.getFullName(),
                checkoutForm.getEmail()
        );
    }
}