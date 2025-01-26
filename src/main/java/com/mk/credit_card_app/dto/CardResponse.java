package com.mk.credit_card_app.dto;

import lombok.Builder;

@Builder
public record CardResponse(ApiResponse response,Card card) {
}
