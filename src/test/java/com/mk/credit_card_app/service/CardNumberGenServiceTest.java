package com.mk.credit_card_app.service;

import com.mk.credit_card_app.repository.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class CardNumberGenServiceTest {

    @Mock
     CreditCardRepository creditCardRepository;

    @InjectMocks
     CardNumberGenService cardNumberGenService;

    @Test
    void testGenerateUniqueCardNumber(){
        when(creditCardRepository.existsByCardNo(anyString())).thenReturn(false);
        String cardNo= cardNumberGenService.generateUniqueCardNumber();
        assertNotNull(cardNo);

    }
}
