package de.oguz.buglab.model;

import java.math.BigDecimal;

public record Product(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean available,
        String category,
        String icon,
        double rating,
        int reviewCount,
        String badge,
        int stock,
        String deliveryInfo
) {
}