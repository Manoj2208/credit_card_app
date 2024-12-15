package com.mk.credit_card_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "credit_cards")
public class CreditCard {
    @Id
    private String cardNo;
    private String expiry;
    private String cvv;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @OneToOne
    private User user;
}
