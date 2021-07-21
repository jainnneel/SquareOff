package com.squareoff.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserHoldingModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hodingId;
    
    @ManyToOne
    private UserEntity userEntity;
    
    @ManyToOne
    private StockEntity stockEntity;
    
    private Long quantity;

    public UserHoldingModel() {
        super();
    }

    public UserHoldingModel(Long hodingId, UserEntity userEntity, StockEntity stockEntity, Long quantity) {
        super();
        this.hodingId = hodingId;
        this.userEntity = userEntity;
        this.stockEntity = stockEntity;
        this.quantity = quantity;
    }

    public Long getHodingId() {
        return hodingId;
    }

    public void setHodingId(Long hodingId) {
        this.hodingId = hodingId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public StockEntity getStockEntity() {
        return stockEntity;
    }

    public void setStockEntity(StockEntity stockEntity) {
        this.stockEntity = stockEntity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "UserHoldingModel [hodingId=" + hodingId + ", userEntity=" + userEntity + ", stockEntity=" + stockEntity
                + ", quantity=" + quantity + "]";
    }
    
    
}
