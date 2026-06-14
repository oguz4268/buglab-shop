package de.oguz.buglab.api;

public record FieldValidationError(
        String field,
        String message
) {
}