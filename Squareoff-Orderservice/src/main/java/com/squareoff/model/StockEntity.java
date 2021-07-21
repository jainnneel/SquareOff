package com.squareoff.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockid;
    
    private String stockName;
    
    private String stockSymbol;
    
    private BigDecimal stockPrice;
    
    private BigDecimal marketcap;
    
    private BigDecimal lowFiftytwoweek;
    
    private BigDecimal highFiftytwoweek;
    
    private BigDecimal stockPe;
    
    @OneToMany(mappedBy = "stockEntity")
    private List<UserHoldingModel> holding;

    public StockEntity() {
        super();
    }

    public StockEntity(Long stockid, String stockName, String stockSymbol, BigDecimal stockPrice, BigDecimal marketcap,
            BigDecimal lowFiftytwoweek, BigDecimal highFiftytwoweek, BigDecimal stockPe) {
        super();
        this.stockid = stockid;
        this.stockName = stockName;
        this.stockSymbol = stockSymbol;
        this.stockPrice = stockPrice;
        this.marketcap = marketcap;
        this.lowFiftytwoweek = lowFiftytwoweek;
        this.highFiftytwoweek = highFiftytwoweek;
        this.stockPe = stockPe;
    }

    public Long getStockid() {
        return stockid;
    }

    public void setStockid(Long stockid) {
        this.stockid = stockid;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

    public BigDecimal getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(BigDecimal marketcap) {
        this.marketcap = marketcap;
    }

    public BigDecimal getLowFiftytwoweek() {
        return lowFiftytwoweek;
    }

    public void setLowFiftytwoweek(BigDecimal lowFiftytwoweek) {
        this.lowFiftytwoweek = lowFiftytwoweek;
    }

    public BigDecimal getHighFiftytwoweek() {
        return highFiftytwoweek;
    }

    public void setHighFiftytwoweek(BigDecimal highFiftytwoweek) {
        this.highFiftytwoweek = highFiftytwoweek;
    }

    public BigDecimal getStockPe() {
        return stockPe;
    }

    public void setStockPe(BigDecimal stockPe) {
        this.stockPe = stockPe;
    }
    
    public List<UserHoldingModel> getHolding() {
        return holding;
    }

    public void setHolding(List<UserHoldingModel> holding) {
        this.holding = holding;
    }

    @Override
    public String toString() {
        return "StockEntity [stockid=" + stockid + ", stockName=" + stockName + ", stockSymbol=" + stockSymbol
                + ", stockPrice=" + stockPrice + ", marketcap=" + marketcap + ", lowFiftytwoweek=" + lowFiftytwoweek
                + ", highFiftytwoweek=" + highFiftytwoweek + ", stockPe=" + stockPe + "]";
    }
    
}
