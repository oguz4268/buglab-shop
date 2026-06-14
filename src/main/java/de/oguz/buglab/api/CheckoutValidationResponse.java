package de.oguz.buglab.api;

public record CheckoutValidationResponse(
        boolean valid,
        String message,
        String customerName,
        String customerEmail
) {
}