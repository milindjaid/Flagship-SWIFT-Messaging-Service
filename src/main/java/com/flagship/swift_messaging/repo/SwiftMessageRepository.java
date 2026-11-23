package com.flagship.swift_messaging.repo;

import com.flagship.swift_messaging.entities.SwiftMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SwiftMessageRepository extends JpaRepository<SwiftMessage, UUID> {
}
