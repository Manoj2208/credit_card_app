package com.mk.credit_card_app.service;

import com.mk.credit_card_app.dto.ApiResponse;
import com.mk.credit_card_app.dto.CardRequest;
import com.mk.credit_card_app.dto.CardResponse;
import com.mk.credit_card_app.dto.CreditCards;
import com.mk.credit_card_app.entity.CreditCard;
import com.mk.credit_card_app.entity.Role;
import com.mk.credit_card_app.entity.User;
import com.mk.credit_card_app.exception.UserConflictsException;
import com.mk.credit_card_app.exception.UserNotFoundException;
import com.mk.credit_card_app.repository.CreditCardRepository;
import com.mk.credit_card_app.repository.UserRepository;
import com.mk.credit_card_app.util.SuccessConstant;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    CreditCardRepository creditCardRepository;
    @Mock
    CardNumberGenService cardNumberGenService;

    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    static final String cardNo="23456789";

    @Test
    void testApplyCreditCardAlreadyRequested(){
        CardRequest cardRequest = CardRequest.builder().firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role("User").build();
        User user=User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();

        when(userRepository.findByPan(anyString())).thenReturn(Optional.of(user));
        assertThrows(UserConflictsException.class,()->userServiceImpl.applyCreditCard(cardRequest));
    }

    @Test
    void testApplyCreditCard(){
        CardRequest cardRequest = CardRequest.builder().firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role("User").build();
        User user=User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();
        CreditCard card=CreditCard.builder().cardNo("2342").cvv("222").expiry("01/29").user(user).build();

        when(cardNumberGenService.generateUniqueCardNumber()).thenReturn(cardNo);
        when(userRepository.findByPan(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(creditCardRepository.save(card)).thenReturn(card);
        ApiResponse apiResponse=userServiceImpl.applyCreditCard(cardRequest);
        assertNotNull(apiResponse);
        assertEquals(SuccessConstant.CARD_APPLIED_MSG,apiResponse.message());
    }

    @Test
    void testGetCardByUserIdUserNotFound(){
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userServiceImpl.getCardByUserId("123-SDW2"));
    }

    @Test
    void testGetCardByUserId(){
        User user=User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();
        CreditCard card=CreditCard.builder().cardNo("16x03HWiSa5PWJKSv6Qa3vYFuW4bDkGiv9g53h4plUs=").cvv("222")
                .expiry("01/29").user(user).build();

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(creditCardRepository.findByUser(user)).thenReturn(card);
        CardResponse cardResponse=userServiceImpl.getCardByUserId("ASODND028-283");
        assertNotNull(cardResponse);
        assertEquals("222",cardResponse.card().cvv());
    }

    @Test
    void testGetCards(){
        User user=User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();
        CreditCard card=CreditCard.builder().cardNo("16x03HWiSa5PWJKSv6Qa3vYFuW4bDkGiv9g53h4plUs=").cvv("222")
                .expiry("01/29").user(user).build();
        List<CreditCard> creditCardList=List.of(card);
        when(creditCardRepository.findAll()).thenReturn(creditCardList);
        CreditCards creditCards=userServiceImpl.getCards();
        assertNotNull(creditCards);
        assertEquals("Test est",creditCards.cards().get(0).cardHolder());
    }


}
