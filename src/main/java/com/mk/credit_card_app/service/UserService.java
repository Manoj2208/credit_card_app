package com.mk.credit_card_app.service;

import com.mk.credit_card_app.dto.ApiResponse;
import com.mk.credit_card_app.dto.CardRequest;
import com.mk.credit_card_app.dto.CardResponse;
import com.mk.credit_card_app.dto.CreditCards;

import java.util.List;

public interface UserService {
    ApiResponse applyCreditCard(CardRequest cardRequest);
    CardResponse getCardByUserId(String userId);
    CreditCards getCards();
}
