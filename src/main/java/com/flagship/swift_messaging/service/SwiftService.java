package com.flagship.swift_messaging.service;

import com.flagship.common_lib.dto.SwiftRejectedEvent;
import com.flagship.common_lib.dto.SwiftSentEvent;
import com.flagship.swift_messaging.entities.SwiftMessage;
import com.flagship.swift_messaging.grpc.PaymentGrpcClient;
import com.flagship.swift_messaging.proto.PaymentResponse;
import com.flagship.swift_messaging.repo.SwiftMessageRepository;
import com.flagship.common_lib.dto.LedgerPendingCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SwiftService {

    private final SwiftMessageRepository swiftMessageRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentGrpcClient paymentGrpcClient;

    @Transactional
    public void processPayment(LedgerPendingCreatedEvent event) {
        log.info("In swift service process payment ...");
        PaymentResponse payment = paymentGrpcClient.getPayment(String.valueOf(event.paymentId()));
        log.info("response ... {}",payment.toString());
        String bic = payment.getBeneficiaryBic();


        // 1️⃣ Build MT103 message
        SwiftMessage mt103 = SwiftMessage.buildMT103(
                event.paymentId(),
                event.amount(),
                event.currency(),
                bic
        );

        // 2️⃣ Persist SWIFT message (audit)
        swiftMessageRepository.save(mt103);

        // 3️⃣ Simulate SWIFT gateway call
        boolean acceptedBySwift = sendToSwiftGateway(mt103); // TODO

        // 4️⃣ Publish result
        if (acceptedBySwift) {
            kafkaTemplate.send(
                    "swift.sent",
                    new SwiftSentEvent(
                            event.paymentId(),
                            mt103.getSwiftReference()
                    )
            );
        } else {
            kafkaTemplate.send(
                    "swift.rejected",
                    new SwiftRejectedEvent(
                            event.paymentId(),
                            "INVALID_BENEFICIARY",
                            UUID.fromString(payment.getCustomerId())
                    )
            );
        }
    }

    private boolean sendToSwiftGateway(SwiftMessage message) {
        // Simulate network + validation
        return Math.random() > 0.1; // 90% success
    }
}


