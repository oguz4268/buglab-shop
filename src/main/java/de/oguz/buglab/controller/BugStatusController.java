package de.oguz.buglab.controller;

import de.oguz.buglab.service.BugToggleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BugStatusController {

    private final BugToggleService bugToggleService;

    public BugStatusController(BugToggleService bugToggleService) {
        this.bugToggleService = bugToggleService;
    }

    @GetMapping("/bug-status")
    public String bugStatus(Model model) {
        List<BugStatusItem> apiBugs = List.of(
                new BugStatusItem("api-001", "Headphones in Warenkorb", "POST /cart/add liefert HTTP 500"),
                new BugStatusItem("api-002", "Laptop Stand Details", "GET /products/4 liefert HTTP 404"),
                new BugStatusItem("api-003", "Suche zuruecksetzen", "GET /products?reset=true liefert HTTP 400"),
                new BugStatusItem("api-004", "Warenkorb -> Weiter einkaufen", "GET /products?fromCart=true liefert HTTP 500"),
                new BugStatusItem("api-005", "Kategorie Adapter & Hubs", "Kategorie-Request liefert HTTP 400"),
                new BugStatusItem("api-006", "Checkout mit Kreditkarte", "POST /checkout liefert HTTP 500"),
                new BugStatusItem("api-007", "Admin-Login", "POST /login liefert HTTP 503"),
                new BugStatusItem("api-008", "Portable SSD entfernen", "POST /cart/remove liefert HTTP 500")
        );

        List<BugStatusItem> uiBugs = List.of(
                new BugStatusItem("ui-001", "Wireless Mouse zweimal hinzufuegen", "Zeilensumme ignoriert Menge"),
                new BugStatusItem("ui-002", "Wireless Mouse Preis", "Produktliste zeigt falschen Preis"),
                new BugStatusItem("ui-003", "Wireless Mouse Rating", "Detailseite zeigt falsches Rating"),
                new BugStatusItem("ui-004", "Audio Trefferanzahl", "Kategorie Audio zeigt falsche Trefferanzahl"),
                new BugStatusItem("ui-005", "Monitore Markierung", "Sidebar markiert falsche Kategorie"),
                new BugStatusItem("ui-006", "Portable SSD Lieferinfo", "Produktkarte zeigt falsche Lieferzeit"),
                new BugStatusItem("ui-007", "27 Inch Monitor Badge", "Produktkarte zeigt falschen Badge"),
                new BugStatusItem("ui-008", "Mechanical Keyboard Warenkorbpreis", "Warenkorb zeigt falschen Einzelpreis"),
                new BugStatusItem("ui-009", "Warenkorb Anzahl", "Anzahl zaehlt Positionen statt Mengen"),
                new BugStatusItem("ui-010", "Bestellbestaetigung Gesamtbetrag", "Gesamtbetrag ist um 7.50 EUR zu hoch"),
                new BugStatusItem("ui-011", "Login Fehlertext", "Falsches Passwort zeigt irrefuehrenden Fehlertext"),
                new BugStatusItem("ui-012", "Customer Rolle", "Customer wird als ADMIN angezeigt")
        );

        model.addAttribute("apiBugs", enrich(apiBugs));
        model.addAttribute("uiBugs", enrich(uiBugs));
        model.addAttribute("activeBugCount", countActive(apiBugs) + countActive(uiBugs));
        model.addAttribute("totalBugCount", apiBugs.size() + uiBugs.size());

        return "bug-status";
    }

    private List<BugStatusView> enrich(List<BugStatusItem> bugs) {
        return bugs.stream()
                .map(item -> new BugStatusView(
                        item.id(),
                        item.title(),
                        item.description(),
                        bugToggleService.isEnabled(item.id())
                ))
                .toList();
    }

    private long countActive(List<BugStatusItem> bugs) {
        return bugs.stream()
                .filter(item -> bugToggleService.isEnabled(item.id()))
                .count();
    }

    private record BugStatusItem(
            String id,
            String title,
            String description
    ) {
    }

    private record BugStatusView(
            String id,
            String title,
            String description,
            boolean enabled
    ) {
    }
}