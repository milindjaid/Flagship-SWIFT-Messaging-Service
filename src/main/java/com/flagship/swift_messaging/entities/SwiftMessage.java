package com.flagship.swift_messaging.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "swift_message",
        indexes = {
                @Index(name = "idx_swift_payment", columnList = "payment_id"),
                @Index(name = "idx_swift_reference", columnList = "swift_reference", unique = true)
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwiftMessage {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(name = "swift_reference", length = 50, unique = true)
    private String swiftReference;

    @Column(name = "message_type", nullable = false, length = 20)
    private String messageType; // e.g. MT103

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ---------- Lifecycle Hooks ----------

    @PrePersist
    public void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public static SwiftMessage buildMT103(
            UUID paymentId,
            BigDecimal amount,
            String currency,
            String bic) {

        SwiftMessage msg = new SwiftMessage();
        msg.id = UUID.randomUUID();
        msg.paymentId = paymentId;
        msg.messageType = "MT103";
        msg.swiftReference = "SWF" + UUID.randomUUID();
        msg.payload =
                "MT103|" + amount + "|" + currency + "|" + bic;
        msg.createdAt = Instant.now();
        msg.updatedAt = Instant.now();
        return msg;
    }
}
