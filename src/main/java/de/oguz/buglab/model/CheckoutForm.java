package de.oguz.buglab.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CheckoutForm {

    @NotBlank(message = "Vorname ist erforderlich.")
    @Size(min = 2, message = "Vorname muss mindestens 2 Zeichen lang sein.")
    private String firstName;

    @NotBlank(message = "Nachname ist erforderlich.")
    @Size(min = 2, message = "Nachname muss mindestens 2 Zeichen lang sein.")
    private String lastName;

    @NotBlank(message = "E-Mail ist erforderlich.")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben.")
    private String email;

    @NotBlank(message = "Straße ist erforderlich.")
    private String street;

    @NotBlank(message = "Postleitzahl ist erforderlich.")
    @Pattern(regexp = "\\d{5}", message = "Postleitzahl muss aus 5 Ziffern bestehen.")
    private String postalCode;

    @NotBlank(message = "Stadt ist erforderlich.")
    private String city;

    @NotBlank(message = "Zahlungsart ist erforderlich.")
    private String paymentMethod;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}