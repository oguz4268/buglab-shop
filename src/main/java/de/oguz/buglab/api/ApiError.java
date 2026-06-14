package de.oguz.buglab.api;

public record ApiError(
        String code,
        String message
) {
}