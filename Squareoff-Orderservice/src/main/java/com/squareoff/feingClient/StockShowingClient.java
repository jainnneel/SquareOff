package com.squareoff.feingClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.squareoff.model.StockEntity;

@FeignClient(name = "STOCK-SHOWING")
public interface StockShowingClient {

    @GetMapping(value = "/stocks/stockInfo/{sid}")
    StockEntity getStockInfo(@PathVariable Long sid);
    
}
