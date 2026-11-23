package com.flagship.swift_messaging.consumers;

import com.flagship.swift_messaging.service.SwiftService;
import com.flagship.common_lib.dto.LedgerPendingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SwiftConsumer {

    private final SwiftService swiftService;

    @KafkaListener(topics = "ledger.pending.created",groupId = "swift-service-group")
    public void onLedgerPendingCreated(LedgerPendingCreatedEvent event, Acknowledgment ack)
    {
        try {
            log.info("In SwiftConsumer...");
            swiftService.processPayment(event);
            ack.acknowledge();
        } catch (RuntimeException e) {
            log.error("Error processing paymentId={}", event.paymentId(), e);
            throw new RuntimeException(e);
        }
    }
}
