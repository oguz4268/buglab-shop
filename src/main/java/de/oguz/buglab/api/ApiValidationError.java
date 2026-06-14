package de.oguz.buglab.api;

import java.util.List;

public record ApiValidationError(
        String code,
        String message,
        List<FieldValidationError> errors
) {
}