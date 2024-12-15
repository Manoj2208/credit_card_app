package com.mk.credit_card_app.repository;

import com.mk.credit_card_app.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard,String> {
}
