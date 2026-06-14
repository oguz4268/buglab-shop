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
                new BugStatusItem(
                        "api-001",
                        "Headphones in Warenkorb",
                        "POST /cart/add liefert HTTP 500",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Noise Cancelling Headphones suchen oder in der Liste finden.",
                                "Button In den Warenkorb klicken.",
                                "Browser DevTools oeffnen und Network-Tab pruefen.",
                                "Request POST /cart/add sollte HTTP 500 liefern."
                        )
                ),
                new BugStatusItem(
                        "api-002",
                        "Laptop Stand Details",
                        "GET /products/4 liefert HTTP 404",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Laptop Stand in der Produktliste finden.",
                                "Details ansehen klicken.",
                                "Network-Tab pruefen.",
                                "Request GET /products/4 sollte HTTP 404 liefern."
                        )
                ),
                new BugStatusItem(
                        "api-003",
                        "Suche zuruecksetzen",
                        "GET /products?reset=true liefert HTTP 400",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Optional einen Suchbegriff eingeben.",
                                "Button Zuruecksetzen klicken.",
                                "Network-Tab pruefen.",
                                "Request GET /products?reset=true sollte HTTP 400 liefern."
                        )
                ),
                new BugStatusItem(
                        "api-004",
                        "Warenkorb -> Weiter einkaufen",
                        "GET /products?fromCart=true liefert HTTP 500",
                        List.of(
                                "Ein verfuegbares Produkt in den Warenkorb legen.",
                                "Warenkorb /cart oeffnen.",
                                "Weiter einkaufen klicken.",
                                "Network-Tab pruefen.",
                                "Request GET /products?fromCart=true sollte HTTP 500 liefern."
                        )
                ),
                new BugStatusItem(
                        "api-005",
                        "Kategorie Adapter & Hubs",
                        "Kategorie-Request liefert HTTP 400",
                        List.of(
                                "Startseite oder Produktseite oeffnen.",
                                "Kategorie Adapter & Hubs anklicken.",
                                "Network-Tab pruefen.",
                                "Request mit category=Adapter%20%26%20Hubs sollte HTTP 400 liefern."
                        )
                ),
                new BugStatusItem(
                        "api-006",
                        "Checkout mit Kreditkarte",
                        "POST /checkout liefert HTTP 500",
                        List.of(
                                "Ein verfuegbares Produkt in den Warenkorb legen.",
                                "Checkout oeffnen.",
                                "Formular mit gueltigen Kundendaten ausfuellen.",
                                "Als Zahlungsart Kreditkarte auswaehlen.",
                                "Bestellung abschliessen.",
                                "Network-Tab pruefen.",
                                "Request POST /checkout sollte HTTP 500 liefern."
                        )
                ),
                new BugStatusItem(
                        "api-007",
                        "Admin-Login",
                        "POST /login liefert HTTP 503",
                        List.of(
                                "Login-Seite /login oeffnen.",
                                "E-Mail admin@example.com eingeben.",
                                "Passwort admin123 eingeben.",
                                "Login absenden.",
                                "Network-Tab pruefen.",
                                "Request POST /login sollte HTTP 503 liefern."
                        )
                ),
                new BugStatusItem(
                        "api-008",
                        "Portable SSD entfernen",
                        "POST /cart/remove liefert HTTP 500",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Portable SSD 1TB in den Warenkorb legen.",
                                "Warenkorb /cart oeffnen.",
                                "Portable SSD entfernen klicken.",
                                "Network-Tab pruefen.",
                                "Request POST /cart/remove sollte HTTP 500 liefern."
                        )
                )
        );

        List<BugStatusItem> uiBugs = List.of(
                new BugStatusItem(
                        "ui-001",
                        "Wireless Mouse zweimal hinzufuegen",
                        "Zeilensumme ignoriert Menge",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Wireless Mouse zweimal in den Warenkorb legen.",
                                "Warenkorb /cart oeffnen.",
                                "Menge sollte 2 sein.",
                                "Korrekt waere eine Zeilensumme von 49,98 EUR.",
                                "Fehlerhaft bleibt die Zeilensumme bei 24,99 EUR."
                        )
                ),
                new BugStatusItem(
                        "ui-002",
                        "Wireless Mouse Preis",
                        "Produktliste zeigt falschen Preis",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Wireless Mouse in der Produktliste finden.",
                                "Preis in der Produktkarte pruefen.",
                                "Korrekt waere 24,99 EUR.",
                                "Fehlerhaft wird 21,99 EUR angezeigt."
                        )
                ),
                new BugStatusItem(
                        "ui-003",
                        "Wireless Mouse Rating",
                        "Detailseite zeigt falsches Rating",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Wireless Mouse in der Liste pruefen: Rating 4.5.",
                                "Details ansehen klicken.",
                                "Rating auf der Detailseite pruefen.",
                                "Korrekt waere ebenfalls 4.5.",
                                "Fehlerhaft wird 3.9 angezeigt."
                        )
                ),
                new BugStatusItem(
                        "ui-004",
                        "Audio Trefferanzahl",
                        "Kategorie Audio zeigt falsche Trefferanzahl",
                        List.of(
                                "Produktseite /products?category=Audio oeffnen.",
                                "Anzahl der sichtbaren Audio-Produkte zaehlen.",
                                "Korrekt waeren 2 Produkte.",
                                "Angezeigte Trefferanzahl im Kopfbereich pruefen.",
                                "Fehlerhaft wird eine zu hohe Trefferanzahl angezeigt."
                        )
                ),
                new BugStatusItem(
                        "ui-005",
                        "Monitore Markierung",
                        "Sidebar markiert falsche Kategorie",
                        List.of(
                                "Produktseite /products?category=Monitore oeffnen.",
                                "Produktliste sollte Monitor-Produkte anzeigen.",
                                "Sidebar-Kategorie-Markierung pruefen.",
                                "Korrekt waere Monitore markiert.",
                                "Fehlerhaft ist Audio markiert."
                        )
                ),
                new BugStatusItem(
                        "ui-006",
                        "Portable SSD Lieferinfo",
                        "Produktkarte zeigt falsche Lieferzeit",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Portable SSD 1TB suchen.",
                                "Lieferinformation in der Produktkarte pruefen.",
                                "Korrekt waere Lieferung in 1-2 Werktagen.",
                                "Fehlerhaft wird Lieferung in 5-7 Werktagen angezeigt."
                        )
                ),
                new BugStatusItem(
                        "ui-007",
                        "27 Inch Monitor Badge",
                        "Produktkarte zeigt falschen Badge",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "27 Inch Monitor suchen.",
                                "Badge in der Produktkarte pruefen.",
                                "Korrekt waere Business Display.",
                                "Fehlerhaft wird Ausverkauft angezeigt."
                        )
                ),
                new BugStatusItem(
                        "ui-008",
                        "Mechanical Keyboard Warenkorbpreis",
                        "Warenkorb zeigt falschen Einzelpreis",
                        List.of(
                                "Produktseite /products oeffnen.",
                                "Mechanical Keyboard in den Warenkorb legen.",
                                "Warenkorb /cart oeffnen.",
                                "Einzelpreis in der Warenkorb-Zeile pruefen.",
                                "Korrekt waere 89,99 EUR.",
                                "Fehlerhaft wird 79,99 EUR angezeigt."
                        )
                ),
                new BugStatusItem(
                        "ui-009",
                        "Warenkorb Anzahl",
                        "Anzahl zaehlt Positionen statt Mengen",
                        List.of(
                                "Wireless Mouse zweimal in den Warenkorb legen.",
                                "Mechanical Keyboard einmal in den Warenkorb legen.",
                                "Warenkorb /cart oeffnen.",
                                "Korrekte Gesamtanzahl waere 3.",
                                "Fehlerhaft wird 2 angezeigt, weil Positionen statt Mengen gezaehlt werden."
                        )
                ),
                new BugStatusItem(
                        "ui-010",
                        "Bestellbestaetigung Gesamtbetrag",
                        "Gesamtbetrag ist um 7.50 EUR zu hoch",
                        List.of(
                                "Ein oder mehrere verfuegbare Produkte in den Warenkorb legen.",
                                "Zwischensumme im Warenkorb merken.",
                                "Checkout mit gueltigen Daten abschliessen.",
                                "Bestellbestaetigung pruefen.",
                                "Korrekt waere derselbe Gesamtbetrag wie im Warenkorb.",
                                "Fehlerhaft ist der Gesamtbetrag um 7,50 EUR zu hoch."
                        )
                ),
                new BugStatusItem(
                        "ui-011",
                        "Login Fehlertext",
                        "Falsches Passwort zeigt irrefuehrenden Fehlertext",
                        List.of(
                                "Login-Seite /login oeffnen.",
                                "E-Mail user@example.com eingeben.",
                                "Ein falsches Passwort eingeben.",
                                "Login absenden.",
                                "Korrekt waere Fehlertext: E-Mail oder Passwort ist falsch.",
                                "Fehlerhaft wird ein irrefuehrender allgemeiner Fehlertext angezeigt."
                        )
                ),
                new BugStatusItem(
                        "ui-012",
                        "Customer Rolle",
                        "Customer wird als ADMIN angezeigt",
                        List.of(
                                "Login-Seite /login oeffnen.",
                                "Mit user@example.com und password123 einloggen.",
                                "Konto-Seite /account oeffnen.",
                                "Korrekt waere Rolle CUSTOMER.",
                                "Fehlerhaft wird Rolle ADMIN angezeigt."
                        )
                )
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
                        item.steps(),
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
            String description,
            List<String> steps
    ) {
    }

    private record BugStatusView(
            String id,
            String title,
            String description,
            List<String> steps,
            boolean enabled
    ) {
    }
}