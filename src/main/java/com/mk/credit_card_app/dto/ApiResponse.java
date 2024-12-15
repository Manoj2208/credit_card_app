package com.mk.credit_card_app.dto;

import lombok.Builder;

@Builder
public record ApiResponse(String message, String httpStatus) {
}
