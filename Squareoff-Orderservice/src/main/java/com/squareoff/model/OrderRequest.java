package com.squareoff.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    private String type;
    
    private String userId;
    
    private Long stockId;
    
    private BigDecimal price;
    
    private Long quantity;
    
    private Date orderDate;
    
    private Time orderTime;

    public OrderRequest() {
        super();
    }

    public OrderRequest(Long orderId, String type, String userId, Long stockId, BigDecimal price, Long quantity,
            Date orderDate, Time orderTime) {
        super();
        this.orderId = orderId;
        this.type = type;
        this.userId = userId;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Time orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "OrderRequest [orderId=" + orderId + ", type=" + type + ", userId=" + userId + ", stockId=" + stockId
                + ", price=" + price + ", quantity=" + quantity + ", orderDate=" + orderDate + ", orderTime="
                + orderTime + "]";
    }
    
}


















