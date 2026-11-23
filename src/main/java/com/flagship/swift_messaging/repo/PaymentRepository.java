package com.flagship.swift_messaging.repo;

import com.flagship.swift_messaging.entities.Payment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<@NonNull  Payment, @NonNull UUID> {
}

