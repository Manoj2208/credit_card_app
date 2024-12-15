package com.mk.credit_card_app.repository;

import com.mk.credit_card_app.entity.CreditCard;
import com.mk.credit_card_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard,String> {
    boolean existsByCardNo(String cardNumber);

    CreditCard findByUser(User user);
}
