package de.oguz.buglab.controller;

import de.oguz.buglab.model.AuthenticatedUser;
import de.oguz.buglab.model.LoginForm;
import de.oguz.buglab.service.AuthService;
import de.oguz.buglab.service.BugToggleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    public static final String AUTH_SESSION_KEY = "authenticatedUser";

    private final AuthService authService;
    private final BugToggleService bugToggleService;

    public AuthController(AuthService authService,
                          BugToggleService bugToggleService) {
        this.authService = authService;
        this.bugToggleService = bugToggleService;
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if (session.getAttribute(AUTH_SESSION_KEY) != null) {
            return "redirect:/account";
        }

        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }

        return "login";
    }

    @PostMapping("/login")
    public String submitLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                              BindingResult bindingResult,
                              Model model,
                              HttpSession session) {

        if (bugToggleService.isEnabled("api-007")
                && loginForm.getEmail() != null
                && loginForm.getEmail().trim().equalsIgnoreCase("admin@example.com")) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "BUG-API-007: Admin login service unavailable"
            );
        }

        if (bindingResult.hasErrors()) {
            return "login";
        }

        Optional<AuthenticatedUser> authenticatedUser =
                authService.authenticate(loginForm.getEmail(), loginForm.getPassword());

        if (authenticatedUser.isEmpty()) {
            if (bugToggleService.isEnabled("ui-011")
                    && loginForm.getEmail() != null
                    && loginForm.getEmail().trim().equalsIgnoreCase("user@example.com")) {
                model.addAttribute("loginError", "Anmeldung aktuell nicht moeglich.");
            } else {
                model.addAttribute("loginError", "E-Mail oder Passwort ist falsch.");
            }

            return "login";
        }

        AuthenticatedUser userToStore = authenticatedUser.get();

        if (bugToggleService.isEnabled("ui-012")
                && userToStore.email().equalsIgnoreCase("user@example.com")) {
            userToStore = new AuthenticatedUser(
                    userToStore.email(),
                    userToStore.fullName(),
                    "ADMIN"
            );
        }

        session.setAttribute(AUTH_SESSION_KEY, userToStore);

        return "redirect:/account";
    }

    @GetMapping("/account")
    public String account(Model model,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        Object user = session.getAttribute(AUTH_SESSION_KEY);

        if (!(user instanceof AuthenticatedUser authenticatedUser)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Bitte melde dich an, um dein Konto zu öffnen."
            );
            return "redirect:/login";
        }

        model.addAttribute("user", authenticatedUser);

        return "account";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session,
                         RedirectAttributes redirectAttributes) {

        session.removeAttribute(AUTH_SESSION_KEY);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Du wurdest erfolgreich abgemeldet."
        );

        return "redirect:/login";
    }
}