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

    /**
     * Generate a unique 12-digit secure card number.
     *
     * @return A unique 12-digit card number.
     */
    public String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (isCardNumberExists(cardNumber)); // Ensure uniqueness
        return AESEncryptDecryptor.encrypt(cardNumber);
    }

    /**
     * Generates a random 12-digit card number.
     *
     * @return A 12-digit card number.
     */
    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder(CARD_NUMBER_LENGTH);
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            int digit = secureRandom.nextInt(10); // Random digit between 0 and 9
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }

    /**
     * Checks if the card number already exists in the database or in-memory set.
     *
     * @param cardNumber The card number to check.
     * @return True if the card number exists, otherwise false.
     */
    private boolean isCardNumberExists(String cardNumber) {
        return creditCardRepository.existsByCardNo(AESEncryptDecryptor.decrypt(cardNumber));

    }

}

