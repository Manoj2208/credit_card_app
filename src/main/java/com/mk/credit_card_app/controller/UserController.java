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

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/credit-cards")
    public ResponseEntity<ApiResponse> applyCard(@Valid @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.applyCreditCard(cardRequest));
    }

    @GetMapping("/credit-cards/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CardResponse> cardDetailsForUser(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCardByUserId(userId));
    }

    @GetMapping("/credit-cards")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreditCards> cardDetails() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCards());
    }
}
