package com.flagship.swift_messaging.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    private UUID id;

    private UUID customerId;
    private BigDecimal amount;
    private String currency;
    private String beneficiaryBic;

    @Enumerated(EnumType.STRING)
    private com.flagship.common_lib.constant.PaymentStatus status;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

//    @Column(name = "beneficiary_account", nullable = false)
//    private String beneficiaryAccount;
}

