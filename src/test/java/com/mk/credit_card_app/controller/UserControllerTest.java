package com.mk.credit_card_app.controller;

import com.mk.credit_card_app.dto.*;
import com.mk.credit_card_app.service.UserService;
import com.mk.credit_card_app.util.SuccessConstant;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    void testApplyCard() {
        CardRequest cardRequest = CardRequest.builder().firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role("User").build();
        ApiResponse ack = ApiResponse.builder().httpStatus(SuccessConstant.CARD_APPLIED_CODE)
                .message(SuccessConstant.CARD_APPLIED_MSG).build();

        when(userService.applyCreditCard(cardRequest)).thenReturn(ack);
        ResponseEntity<ApiResponse> response = userController.applyCard(cardRequest);
        assertNotNull(response);
        assertEquals("2001", response.getBody().httpStatus());
    }

    @Test
    @WithMockUser(roles = {"USER,ADMIN"})
    void testCardDetailsForUser() {
        CardResponse cardResponse = CardResponse.builder().card(Card.builder().cardHolder("Test kumar").cardNumber("2342")
                .cvv("222").expiry("01/29").build()).response(ApiResponse.builder()
                .httpStatus(SuccessConstant.CARD_DETAILS_FOUND_CODE).message(SuccessConstant.CARD_DETAILS_FOUND_MESSAGE)
                .build()).build();

        when(userService.getCardByUserId(anyString())).thenReturn(cardResponse);
        ResponseEntity<CardResponse> response = userController.cardDetailsForUser("12345");
        assertNotNull(response);
        assertEquals("2003", response.getBody().response().httpStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCardDetails(){
        CreditCards creditCards=CreditCards.builder().cards(List.of(Card.builder().cardHolder("Test kumar")
                .cardNumber("2342").cvv("222").expiry("01/29").build())).response(ApiResponse.builder()
                .httpStatus(SuccessConstant.CARD_DETAILS_FOUND_CODE).message(SuccessConstant.CARD_DETAILS_FOUND_MESSAGE)
                .build()).build();
        when(userService.getCards()).thenReturn(creditCards);
        ResponseEntity<CreditCards> response=userController.cardDetails();
        assertNotNull(response);
        assertEquals("Card Details Fetched Successfully",response.getBody().response().message());
    }

}
