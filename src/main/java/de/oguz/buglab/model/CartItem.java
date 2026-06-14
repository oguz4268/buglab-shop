package de.oguz.buglab.model;

import java.math.BigDecimal;

public record CartItem(
        Product product,
        int quantity,
        boolean ignoreQuantityInLineTotal
) {
    public CartItem(Product product, int quantity) {
        this(product, quantity, false);
    }

    public BigDecimal lineTotal() {
        if (ignoreQuantityInLineTotal) {
            return product.price();
        }

        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}