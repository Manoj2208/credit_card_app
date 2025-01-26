package com.mk.credit_card_app.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CreditCards(List<Card> cards, ApiResponse response) {
}
