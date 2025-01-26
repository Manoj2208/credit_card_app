package com.mk.credit_card_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(@NotBlank(message = "userName is required")
                          String userName, @NotBlank(message = "password is required") String password) {

}
