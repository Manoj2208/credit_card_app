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
import com.mk.credit_card_app.util.AESEncryptDecryptor;
import com.mk.credit_card_app.util.CardGenerator;
import com.mk.credit_card_app.util.SuccessConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardNumberGenService cardNumberGenService;

    @Override
    public ApiResponse applyCreditCard(CardRequest cardRequest) {
        userRepository.findByPan(cardRequest.pan()).ifPresent(user -> {
            log.error("User with pan already exists");
            throw new UserConflictsException();
        });

        User user = userMapper(cardRequest);
        CreditCard creditCard = creditCardMapper(user);

        log.info("User registered for the credit card");
        userRepository.save(user);
        log.info("Credit card applied for the user");
        creditCardRepository.save(creditCard);

        return ApiResponse.builder()
                .httpStatus(SuccessConstant.CARD_APPLIED_CODE)
                .message(SuccessConstant.CARD_APPLIED_MSG)
                .build();
    }

    @Override
    public CardResponse getCardByUserId(String userId) {
        return Optional.of(userRepository.findById(userId))
                .map(user -> {
                    CreditCard creditCard = creditCardRepository.findByUser(user.get());
                    return CardResponse.builder()
                            .expiry(creditCard.getExpiry())
                            .cvv(creditCard.getCvv())
                            .cardHolder(user.get().getFirstName() + " " + user.get().getLastName())
                            .cardNumber(AESEncryptDecryptor.decrypt(creditCard.getCardNo()))
                            .response(ApiResponse.builder()
                                    .httpStatus(SuccessConstant.CARD_DETAILS_FOUND_CODE)
                                    .message(SuccessConstant.CARD_DETAILS_FOUND_MESSAGE)
                                    .build())
                            .build();
                })
                .orElseThrow(() -> {
                    log.error(String.format("User: %s not found", userId));
                    return new UserNotFoundException();
                });
    }


    @Override
    public CreditCards getCards() {
        List<CardResponse> cardResponses = creditCardRepository.findAll().stream().map(card -> CardResponse.builder()
                .expiry(card.getExpiry()).cvv(card.getCvv()).cardNumber(AESEncryptDecryptor
                        .decrypt(card.getCardNo())).cardHolder(card.getUser().getFirstName() + " " + card.getUser().
                        getLastName()).build()).toList();
        return CreditCards.builder().cards(cardResponses).response(ApiResponse.builder().message(SuccessConstant.
                CARD_DETAILS_FOUND_MESSAGE).httpStatus(SuccessConstant.CARD_DETAILS_FOUND_CODE).build()).build();
    }

    private User userMapper(CardRequest cardRequest) {
        String userId = UUID.randomUUID().toString();
        return User.builder().userId(userId).firstName(cardRequest.firstName())
                .lastName(cardRequest.lastName()).email(cardRequest.email()).password(passwordEncoder.
                        encode(cardRequest.password())).pan(cardRequest.pan()).
                role(Role.valueOf(cardRequest.role().toUpperCase())).
                build();
    }

    private CreditCard creditCardMapper(User user) {
        return CreditCard.builder().cardNo(cardNumberGenService.generateUniqueCardNumber()).expiry(CardGenerator
                        .generateExpiryDate()).
                cvv(CardGenerator.generateCVV()).user(user).build();
    }
}
