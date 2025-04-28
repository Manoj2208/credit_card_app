package com.mk.credit_card_app.controller;

import com.mk.credit_card_app.dto.ApiResponse;
import com.mk.credit_card_app.dto.CardRequest;
import com.mk.credit_card_app.dto.CardResponse;
import com.mk.credit_card_app.dto.CreditCards;
import com.mk.credit_card_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling credit card-related operations.
 * <p>
 * This controller allows users to apply for a credit card and retrieve card details.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint to apply for a credit card.
     *
     * @param cardRequest the credit card application request containing user and card details
     * @return a response indicating the application status
     */
    @PostMapping("/user/credit-cards")
    public ResponseEntity<ApiResponse> applyCard(@Valid @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.applyCreditCard(cardRequest));
    }

    /**
     * Retrieves credit card details for a specific user.
     *
     * @param userId the unique identifier of the user
     * @return the credit card details associated with the user
     */
    @GetMapping("/credit-cards/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CardResponse> cardDetailsForUser(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCardByUserId(userId));
    }

    /**
     * Retrieves all credit card applications.
     * <p>
     * Accessible only by users with the ADMIN role.
     * </p>
     *
     * @return a list of all credit card applications
     */
    @GetMapping("/credit-cards")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreditCards> cardDetails() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCards());
    }
}
