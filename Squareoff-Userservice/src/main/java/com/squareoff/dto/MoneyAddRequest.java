package com.squareoff.dto;

import java.math.BigDecimal;

public class MoneyAddRequest {

    private BigDecimal amount;

    public MoneyAddRequest() {
        super();
    }

    public MoneyAddRequest(BigDecimal amount) {
        super();
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MoneyAddRequest [amount=" + amount + "]";
    }
    
    
    
}
