package com.notification;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class OrderSuccess {

    private Long orderSuccessId;
    
    private Long orderId;
    
    private String type;
    
    private String userId;
    
    private Long stockId;
    
    private BigDecimal price;
    
    private Long quantity;
    
    private Date orderDate;
    
    private Time orderTime;

    public OrderSuccess() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OrderSuccess(Long orderSuccessId, Long orderId, String type, String userId, Long stockId, BigDecimal price,
            Long quantity, Date orderDate, Time orderTime) {
        super();
        this.orderSuccessId = orderSuccessId;
        this.orderId = orderId;
        this.type = type;
        this.userId = userId;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "OrderSuccess [orderSuccessId=" + orderSuccessId + ", orderId=" + orderId + ", type=" + type
                + ", userId=" + userId + ", stockId=" + stockId + ", price=" + price + ", quantity=" + quantity
                + ", orderDate=" + orderDate + ", orderTime=" + orderTime + "]";
    }

    public Long getOrderSuccessId() {
        return orderSuccessId;
    }

    public void setOrderSuccessId(Long orderSuccessId) {
        this.orderSuccessId = orderSuccessId;
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
    
    
    
}
