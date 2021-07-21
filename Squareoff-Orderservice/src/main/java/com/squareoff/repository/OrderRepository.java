package com.squareoff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squareoff.model.OrderRequest;

@Repository
public interface OrderRepository extends JpaRepository<OrderRequest, Long> {
    
    @Query(value = "select order from OrderRequest as order where order.type=?1 and order.stockId=?2")
    List<OrderRequest> getAllorder(String type, Long stockId);

}
