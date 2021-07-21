package com.squareoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squareoff.model.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

}
