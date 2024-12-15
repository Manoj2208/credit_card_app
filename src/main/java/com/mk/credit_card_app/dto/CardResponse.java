package com.mk.credit_card_app.dto;

import lombok.Builder;

@Builder
public record CardResponse(ApiResponse response,String cardHolder,String expiry,String cvv) {
}
