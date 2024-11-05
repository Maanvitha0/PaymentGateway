package com.example.repository;

import com.example.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    // Custom query methods can be defined here if needed
}
