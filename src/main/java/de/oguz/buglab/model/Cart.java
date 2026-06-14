package de.oguz.buglab.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    private boolean countPositionsInsteadOfQuantity;

    public void setCountPositionsInsteadOfQuantity(boolean countPositionsInsteadOfQuantity) {
        this.countPositionsInsteadOfQuantity = countPositionsInsteadOfQuantity;
    }

    public void addProduct(Product product) {
        addProduct(product, false);
    }

    public void addProduct(Product product, boolean ignoreQuantityInLineTotal) {
        CartItem existingItem = items.get(product.id());

        if (existingItem == null) {
            items.put(product.id(), new CartItem(product, 1, ignoreQuantityInLineTotal));
        } else {
            items.put(
                    product.id(),
                    new CartItem(
                            product,
                            existingItem.quantity() + 1,
                            existingItem.ignoreQuantityInLineTotal() || ignoreQuantityInLineTotal
                    )
            );
        }
    }

    public void removeProduct(Long productId) {
        items.remove(productId);
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public BigDecimal getSubtotal() {
        return items.values().stream()
                .map(CartItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalQuantity() {
        if (countPositionsInsteadOfQuantity) {
            return items.size();
        }

        return items.values().stream()
                .mapToInt(CartItem::quantity)
                .sum();
    }

    public int getRealTotalQuantity() {
        return items.values().stream()
                .mapToInt(CartItem::quantity)
                .sum();
    }

    public int getPositionCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }
}