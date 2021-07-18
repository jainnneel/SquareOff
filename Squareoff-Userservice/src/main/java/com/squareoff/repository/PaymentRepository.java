package com.squareoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squareoff.model.AddPayment;

@Repository
public interface PaymentRepository  extends JpaRepository<AddPayment, Long> {

    @Query(value = "select ap from AddPayment as ap where ap.orderId=?1")
    AddPayment getOrderByOrderId(String orderId);

}
