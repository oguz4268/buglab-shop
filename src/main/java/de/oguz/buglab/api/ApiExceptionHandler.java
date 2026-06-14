package de.oguz.buglab.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationError> handleValidationError(MethodArgumentNotValidException exception) {

        Map<String, String> firstErrorByField = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted(Comparator.comparing(error -> error.getField()))
                .forEach(error ->
                        firstErrorByField.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        List<FieldValidationError> errors = firstErrorByField.entrySet()
                .stream()
                .map(entry -> new FieldValidationError(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();

        ApiValidationError response = new ApiValidationError(
                "VALIDATION_ERROR",
                "Die übergebenen Daten sind ungültig.",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }
}