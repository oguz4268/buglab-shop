package de.oguz.buglab.service;

import de.oguz.buglab.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class ProductService {

    private final List<Product> products = List.of(
            new Product(1L, "Wireless Mouse", "Ergonomische kabellose Maus für Büro und Homeoffice", new BigDecimal("24.99"), true, "Mäuse", "🖱️", 4.5, 128, "Bestseller", 42, "Lieferung in 2–3 Werktagen"),
            new Product(2L, "Mechanical Keyboard", "Mechanische Tastatur mit deutschem Layout und präzisem Schreibgefühl", new BigDecimal("89.99"), true, "Tastaturen", "⌨️", 4.8, 96, "Pro Setup", 18, "Lieferung in 2–3 Werktagen"),
            new Product(3L, "USB-C Hub", "USB-C Hub mit HDMI, USB und Ethernet für flexible Arbeitsplätze", new BigDecimal("39.99"), true, "Adapter & Hubs", "🔌", 4.4, 74, "Top bewertet", 31, "Lieferung in 1–2 Werktagen"),
            new Product(4L, "Laptop Stand", "Aluminium Laptopständer für ergonomisches Arbeiten", new BigDecimal("34.99"), false, "Arbeitsplatz", "💻", 4.2, 51, "Ergonomie", 0, "Derzeit nicht lieferbar"),
            new Product(5L, "Noise Cancelling Headphones", "Kopfhörer mit aktiver Geräuschunterdrückung für konzentrierte Meetings", new BigDecimal("129.99"), true, "Audio", "🎧", 4.6, 142, "Audio Highlight", 12, "Lieferung in 2–3 Werktagen"),
            new Product(6L, "4K Webcam", "Hochauflösende Webcam für Videokonferenzen und hybride Meetings", new BigDecimal("74.99"), true, "Video", "📷", 4.5, 83, "Remote Work", 22, "Lieferung in 2–3 Werktagen"),
            new Product(7L, "Bluetooth Speaker", "Kompakter Lautsprecher mit klarem Sound für Büro und Zuhause", new BigDecimal("49.99"), true, "Audio", "🔊", 4.3, 67, "Kompakt", 27, "Lieferung in 2–3 Werktagen"),
            new Product(8L, "Portable SSD 1TB", "Externe SSD mit 1 TB Speicher und schneller USB-C Verbindung", new BigDecimal("109.99"), true, "Speicher", "💾", 4.7, 119, "Performance", 16, "Lieferung in 1–2 Werktagen"),
            new Product(9L, "27 Inch Monitor", "QHD Monitor mit 27 Zoll für produktive Arbeitsplätze", new BigDecimal("249.99"), true, "Monitore", "🖥️", 4.6, 88, "Business Display", 9, "Lieferung in 3–4 Werktagen"),
            new Product(10L, "Ergonomic Desk Lamp", "LED-Schreibtischlampe mit einstellbarer Helligkeit", new BigDecimal("44.99"), true, "Arbeitsplatz", "💡", 4.1, 39, "Desk Setup", 24, "Lieferung in 2–3 Werktagen")
    );

    public List<Product> findAll() {
        return products;
    }

    public List<String> findCategories() {
        return products.stream()
                .map(Product::category)
                .distinct()
                .sorted()
                .toList();
    }

    public List<Product> search(String query) {
        return search(query, null);
    }

    public List<Product> search(String query, String category) {
        return products.stream()
                .filter(product -> matchesQuery(product, query))
                .filter(product -> matchesCategory(product, category))
                .toList();
    }

    public Product findById(Long id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produkt nicht gefunden: " + id));
    }

    private boolean matchesQuery(Product product, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }

        String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);

        return product.name().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                || product.description().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                || product.category().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                || product.badge().toLowerCase(Locale.ROOT).contains(normalizedQuery);
    }

    private boolean matchesCategory(Product product, String category) {
        if (category == null || category.isBlank()) {
            return true;
        }

        return product.category().equalsIgnoreCase(category.trim());
    }
}