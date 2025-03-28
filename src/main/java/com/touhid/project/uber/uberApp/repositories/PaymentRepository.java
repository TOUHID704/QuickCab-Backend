package com.touhid.project.uber.uberApp.repositories;

import com.touhid.project.uber.uberApp.entities.Payment;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByRide(Ride ride);

}
