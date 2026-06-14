package de.oguz.buglab.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginForm {

    @NotBlank(message = "E-Mail ist erforderlich.")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben.")
    private String email;

    @NotBlank(message = "Passwort ist erforderlich.")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}