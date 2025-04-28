package com.mk.credit_card_app.service;

import com.mk.credit_card_app.repository.CreditCardRepository;
import com.mk.credit_card_app.util.AESEncryptDecryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Service responsible for generating unique credit card numbers.
 * <p>
 * Ensures that the generated card number does not already exist in the system,
 * and returns the encrypted version for storage.
 * </p>
 */
@Service
public class CardNumberGenService {

    private static final int CARD_NUMBER_LENGTH = 12;
    private static final SecureRandom secureRandom = new SecureRandom();

    private final CreditCardRepository creditCardRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param creditCardRepository repository to check for existing card numbers
     */
    @Autowired
    public CardNumberGenService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    /**
     * Generates a unique 12-digit card number and encrypts it before returning.
     *
     * @return encrypted unique card number
     */
    public String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (isCardNumberExists(cardNumber));

        return AESEncryptDecryptor.encrypt(cardNumber);
    }

    /**
     * Generates a random 12-digit numeric card number.
     *
     * @return a plain-text card number string
     */
    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder(CARD_NUMBER_LENGTH);
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            int digit = secureRandom.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }

    /**
     * Checks whether a given plain-text card number already exists in the database.
     * It assumes that the card numbers in the database are stored in encrypted form.
     *
     * @param cardNumber plain-text card number to check
     * @return true if the card number already exists, false otherwise
     */
    private boolean isCardNumberExists(String cardNumber) {
        // Encrypt before checking since DB stores encrypted values
        String encryptedCardNumber = AESEncryptDecryptor.encrypt(cardNumber);
        return creditCardRepository.existsByCardNo(encryptedCardNumber);
    }
}
