package com.squareoff.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.squareoff.model.StockEntity;

@Service
public interface StockRepository  extends JpaRepository<StockEntity,Long> {

    @Query(value = "select st from StockEntity as st where st.stockName LIKE %:keyword% OR st.stockSymbol LIKE %:keyword%",countQuery = "select count(st) from StockEntity as st")
    Page<StockEntity> findStocks(@Param("keyword") String keyword, Pageable pageable);

}
