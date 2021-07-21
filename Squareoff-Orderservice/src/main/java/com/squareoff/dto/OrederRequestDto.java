package com.squareoff.dto;

import java.math.BigDecimal;

public class OrederRequestDto {

    private String type;

    private Long stockId;

    private BigDecimal price;

    private Long quantity;

    public OrederRequestDto() {
        super();
    }

    public OrederRequestDto(String type, Long stockId, BigDecimal price, Long quantity) {
        super();
        this.type = type;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "OrederRequestDto [type=" + type + ", stockId=" + stockId + ", price=" + price + ", quantity=" + quantity
                + "]";
    }
    
    
}
