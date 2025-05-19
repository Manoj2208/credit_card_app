package com.mk.credit_card_app.repository;

import com.mk.credit_card_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByPan(String panNumber);
}
