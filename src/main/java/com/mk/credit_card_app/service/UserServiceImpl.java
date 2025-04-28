package com.mk.credit_card_app.service;

import com.mk.credit_card_app.dto.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for user and credit card related operations.
 * <p>
 * Handles applying for a credit card, fetching card details by user ID,
 * and retrieving all issued cards (admin use).
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardNumberGenService cardNumberGenService;

    /**
     * Allows a user to apply for a credit card.
     * Validates that the PAN number doesn't already exist.
     *
     * @param cardRequest request containing user and card application details
     * @return API response indicating success or conflict
     * @throws UserConflictsException if a user with the given PAN already exists
     */
    @Override
    @Transactional
    public ApiResponse applyCreditCard(CardRequest cardRequest) {
        userRepository.findByPan(cardRequest.pan()).ifPresent(user -> {
            log.error("User with PAN already exists");
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

    /**
     * Retrieves credit card details for a specific user by user ID.
     *
     * @param userId unique identifier of the user
     * @return a response containing card details
     * @throws UserNotFoundException if the user ID is not found in the system
     */
    @Override
    public CardResponse getCardByUserId(String userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    CreditCard creditCard = creditCardRepository.findByUser(user);
                    return CardResponse.builder()
                            .card(Card.builder()
                                    .expiry(creditCard.getExpiry())
                                    .cvv(creditCard.getCvv())
                                    .cardHolder(user.getFirstName() + " " + user.getLastName())
                                    .cardNumber(AESEncryptDecryptor.decrypt(creditCard.getCardNo()))
                                    .build())
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

    /**
     * Retrieves all credit cards issued in the system.
     * Intended for admin usage.
     *
     * @return a wrapper containing a list of all credit cards and API response
     */
    @Override
    public CreditCards getCards() {
        List<Card> cardResponses = creditCardRepository.findAll().stream().map(card -> Card.builder()
                .expiry(card.getExpiry())
                .cvv(card.getCvv())
                .cardNumber(AESEncryptDecryptor.decrypt(card.getCardNo()))
                .cardHolder(card.getUser().getFirstName() + " " + card.getUser().getLastName())
                .build()).toList();

        return CreditCards.builder()
                .cards(cardResponses)
                .response(ApiResponse.builder()
                        .message(SuccessConstant.CARD_DETAILS_FOUND_MESSAGE)
                        .httpStatus(SuccessConstant.CARD_DETAILS_FOUND_CODE)
                        .build())
                .build();
    }

    /**
     * Maps a {@link CardRequest} to a {@link User} entity,
     * including UUID generation and role conversion.
     *
     * @param cardRequest the request data from the user
     * @return a fully built User object
     */
    private User userMapper(CardRequest cardRequest) {
        String userId = UUID.randomUUID().toString();
        return User.builder()
                .userId(userId)
                .firstName(cardRequest.firstName())
                .lastName(cardRequest.lastName())
                .email(cardRequest.email())
                .password(passwordEncoder.encode(cardRequest.password()))
                .pan(cardRequest.pan())
                .role(Role.valueOf(cardRequest.role().toUpperCase()))
                .build();
    }

    /**
     * Maps a {@link User} to a {@link CreditCard} with generated card data.
     *
     * @param user the user to whom the credit card is linked
     * @return a new CreditCard object
     */
    private CreditCard creditCardMapper(User user) {
        return CreditCard.builder()
                .cardNo(cardNumberGenService.generateUniqueCardNumber())
                .expiry(CardGenerator.generateExpiryDate())
                .cvv(CardGenerator.generateCVV())
                .user(user)
                .build();
    }
}
