package com.squareoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squareoff.model.OrderSuccess;

@Repository
public interface OrderSuccessRepository extends JpaRepository<OrderSuccess,Long> {

}
