package com.example.cognitivetwin.common.idempotency;

import com.example.cognitivetwin.common.ApiResponse;
import com.example.cognitivetwin.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "idempotency")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyEntity extends BaseEntity {

    @Column(name = "key",unique = true,nullable = false)
    private String key;

    @Column(nullable = false)
    private String requestHash;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdempotencyStatus idempotencyStatus;

    @Column(nullable = false)
    private Instant expiryDate;
}
