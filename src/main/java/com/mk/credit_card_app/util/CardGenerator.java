package com.mk.credit_card_app.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CardGenerator {
    public static String generateExpiryDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = currentDate.plusYears(4); // Add 4 years
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return expiryDate.format(formatter);
    }

    public static String generateCVV() {
        SecureRandom random = new SecureRandom();
        int cvv = random.nextInt(900) + 100; // Generate a number between 100 and 999
        return String.valueOf(cvv);
    }
}
