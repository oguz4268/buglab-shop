package de.oguz.buglab.model;

import java.io.Serializable;

public record AuthenticatedUser(
        String email,
        String fullName,
        String role
) implements Serializable {
}