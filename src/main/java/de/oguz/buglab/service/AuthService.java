package de.oguz.buglab.service;

import de.oguz.buglab.model.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final Map<String, DemoUser> users = Map.of(
            "user@example.com", new DemoUser("user@example.com", "password123", "Max Mustermann", "CUSTOMER"),
            "admin@example.com", new DemoUser("admin@example.com", "admin123", "Admin User", "ADMIN")
    );

    public Optional<AuthenticatedUser> authenticate(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }

        DemoUser user = users.get(email.trim().toLowerCase());

        if (user == null) {
            return Optional.empty();
        }

        if (!user.password().equals(password)) {
            return Optional.empty();
        }

        return Optional.of(new AuthenticatedUser(
                user.email(),
                user.fullName(),
                user.role()
        ));
    }

    private record DemoUser(
            String email,
            String password,
            String fullName,
            String role
    ) {
    }
}