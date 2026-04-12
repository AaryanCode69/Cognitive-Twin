package com.example.cognitivetwin.common.idempotency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdempotencyRepository extends JpaRepository<IdempotencyEntity, UUID> {
    IdempotencyEntity findByKey(String key);
}
