package com.squareoff.serviceimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.squareoff.model.StockEntity;
import com.squareoff.repository.StockRepository;

@Service
public class StockserviceImplimentation {

    
    @Autowired
    private StockRepository stockRepository;
    
    public void addData(List<StockEntity> entities) {
        stockRepository.saveAll(entities);
    }
   
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader  = new BufferedReader(new FileReader(new File("C:\\Users\\91953\\Downloads\\NSE - Sheet1.csv")));
        
        List<StockEntity> entities = new ArrayList<>();
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
        System.out.println();
        StockserviceImplimentation implimentation = new StockserviceImplimentation();
        implimentation.addData(entities);
    }

    public List<StockEntity> getAllStocks(String keyword , int pageNo) {
        
        Pageable pageable = PageRequest.of(pageNo - 1, 10,Sort.by(Direction.DESC, "stockPrice"));
        
        List<StockEntity> entities = stockRepository.findStocks(keyword,pageable).getContent();
        
        return entities;
    }

    public StockEntity getStockInfo(Long sid) {
        Optional<StockEntity> findById = stockRepository.findById(sid);
        
        if (findById.isPresent()) {
            return findById.get();
        }
        return null;
    }
    
}






























