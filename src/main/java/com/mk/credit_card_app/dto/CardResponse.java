package com.mk.credit_card_app.dto;

import lombok.Builder;

@Builder
public record CardResponse(String cardNumber,String cardHolder,String expiry,String cvv) {
}
