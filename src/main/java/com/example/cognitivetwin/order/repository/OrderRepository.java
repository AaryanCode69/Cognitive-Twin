package com.example.cognitivetwin.order.repository;

import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> , JpaSpecificationExecutor<OrderEntity> {
    boolean existsById(UUID uuid);

    Page<OrderEntity> findByUser(UserEntity user, Pageable pageable);
}
