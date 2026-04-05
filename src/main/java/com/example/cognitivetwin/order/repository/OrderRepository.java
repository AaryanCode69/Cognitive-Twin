package com.example.cognitivetwin.order.repository;

import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    boolean existsById(UUID uuid);

    List<OrderEntity> findByUser(UserEntity user);
}
