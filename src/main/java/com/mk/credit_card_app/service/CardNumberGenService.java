package com.mk.credit_card_app.service;

import com.mk.credit_card_app.repository.CreditCardRepository;
import com.mk.credit_card_app.util.AESEncryptDecryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public class CardNumberGenService {
    private static final int CARD_NUMBER_LENGTH = 12;
    private static final SecureRandom secureRandom = new SecureRandom();
    private final CreditCardRepository creditCardRepository;

    @Autowired
    public CardNumberGenService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (isCardNumberExists(cardNumber));

        // Encrypt only once before returning/saving
        return AESEncryptDecryptor.encrypt(cardNumber);
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder(CARD_NUMBER_LENGTH);
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            int digit = secureRandom.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }

    private boolean isCardNumberExists(String cardNumber) {
        // Do not decrypt here; store encrypted versions in the database
        return creditCardRepository.existsByCardNo(cardNumber);
    }
}


