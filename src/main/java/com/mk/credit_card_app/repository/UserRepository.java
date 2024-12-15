package com.mk.credit_card_app.repository;

import com.mk.credit_card_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
