package com.mk.credit_card_app.dto;

import lombok.Builder;

@Builder
public record AuthResponse(ApiResponse response,String jwtToken) {
}
