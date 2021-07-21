package com.squareoff.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squareoff.dto.ResponseEntity;
import com.squareoff.model.StockEntity;
import com.squareoff.serviceimpl.StockserviceImplimentation;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockserviceImplimentation implimentation;
    
    @GetMapping("/search/{keyword}/{pageno}")
    public ResponseEntity getStocks(@PathVariable("keyword") String keyword,@PathVariable("pageno") int pageno) {
        ResponseEntity entity = new ResponseEntity();
        
        List<StockEntity> entities = implimentation.getAllStocks(keyword,pageno);
        entity.setData(entities);
        return entity;
        
    }
    
    @GetMapping("/")
    public String getStocdks() {
       return "dsa";        
    }
    
    @GetMapping("/stockInfo/{sid}")
    public StockEntity getStocksInfo(@PathVariable Long sid) {
       
        return implimentation.getStockInfo(sid);
        
    }
    
    
    
    @GetMapping("/adddata")
    public String add() {
        List<StockEntity> entities;
        try {
            BufferedReader bufferedReader  = new BufferedReader(new FileReader(new File("C:\\Users\\91953\\Downloads\\NSE - Sheet1.csv")));
            
            entities = new ArrayList<>();
            String line = "";
            int i = 0;
            while ((line = bufferedReader.readLine())!=null) {
               if (i== 0) {
                   i = 1;
               }else {
                   StockEntity entity = new StockEntity();
                   String[] str =  line.split(",");
                   if (!str[0].equals("#N/A")) entity.setStockSymbol(str[0]);
                   if (!str[1].equals("#N/A")) entity.setStockName(str[1]);
                   if (!str[2].equals("#N/A")) entity.setStockPrice(BigDecimal.valueOf(Double.parseDouble(str[2])));
                   if (!str[3].equals("#N/A")) entity.setMarketcap(BigDecimal.valueOf(Double.parseDouble(str[3])));
                   if (!str[4].equals("#N/A")) entity.setLowFiftytwoweek(BigDecimal.valueOf(Double.parseDouble(str[4])));
                   if (!str[5].equals("#N/A")) entity.setHighFiftytwoweek(BigDecimal.valueOf(Double.parseDouble(str[5])));
                   if (!str[6].equals("#N/A")) entity.setStockPe(BigDecimal.valueOf(Double.parseDouble(str[6])));
                   
                   entities.add(entity);
                   System.out.println(entity);
               } 
            }
            implimentation.addData(entities);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        System.out.println();
        return "done";
    }
}
