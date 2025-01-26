package com.mk.credit_card_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CardRequest(@NotBlank(message = "firstName is required") String firstName,
                          String lastName,
                          @Email(message = "invalid email")
                          String email,
                          @NotBlank(message = "password is required") @Pattern(regexp =
                                  "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                                  message = "password should match the standard format")
                          String password,
                          @NotBlank(message = "Pan card number is required.") @Pattern(regexp =
                                  "^[A-Z]{5}[\\d]{4}[A-Z]$", message = "Invalid pan number") String pan,
                          @NotBlank(message = "role is required") @Pattern(message = "invalid role", regexp =
                                  "^(?i)(user|admin)$")
                          String role) {
}
