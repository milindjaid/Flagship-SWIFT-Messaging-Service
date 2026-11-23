package com.flagship.swift_messaging.grpc;

import com.flagship.swift_messaging.proto.PaymentQueryServiceGrpc;
import com.flagship.swift_messaging.proto.PaymentRequest;
import com.flagship.swift_messaging.proto.PaymentResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class PaymentGrpcClient {

    private final PaymentQueryServiceGrpc.PaymentQueryServiceBlockingStub blockingStub;
// forAddress("payment-service", 9090)
//(or payment-service.namespace.svc.cluster.local if you’re being explicit) for k8s
    public PaymentGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090) // if šervice is on same machine
                .usePlaintext() // ⚠️ remove in prod (use TLS)
                .build();

        this.blockingStub = PaymentQueryServiceGrpc.newBlockingStub(channel);
    }

    public PaymentResponse getPayment(String paymentId) {
        PaymentRequest request = PaymentRequest.newBuilder()
                .setPaymentId(paymentId)
                .build();

        return blockingStub.getPayment(request);
    }
}
