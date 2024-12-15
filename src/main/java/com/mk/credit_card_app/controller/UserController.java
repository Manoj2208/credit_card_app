package com.mk.credit_card_app.controller;

import com.mk.credit_card_app.dto.ApiResponse;
import com.mk.credit_card_app.dto.CardRequest;
import com.mk.credit_card_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/credit-cards")
    public ResponseEntity<ApiResponse> applyCard(@Valid @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.applyCreditCard(cardRequest));
    }
}
