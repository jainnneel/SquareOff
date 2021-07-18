package com.squareoff.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AddPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addreqId;
    
    private String orderId;
    
    private String userId;
    
    private String paymentId;
    
    private BigDecimal amount;
    
    private Date paymentDate;
    
    private Time paymentTime;
    
    private boolean isSucess;

    public AddPayment() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AddPayment(Long addreqId, String orderId, String userId, String paymentId, BigDecimal amount,
            Date paymentDate, Time paymentTime, boolean isSucess) {
        super();
        this.addreqId = addreqId;
        this.orderId = orderId;
        this.userId = userId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.isSucess = isSucess;
    }

    public Long getAddreqId() {
        return addreqId;
    }

    public void setAddreqId(Long addreqId) {
        this.addreqId = addreqId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Time getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Time paymentTime) {
        this.paymentTime = paymentTime;
    }

    public boolean isSucess() {
        return isSucess;
    }

    public void setSucess(boolean isSucess) {
        this.isSucess = isSucess;
    }

    @Override
    public String toString() {
        return "AddPayment [addreqId=" + addreqId + ", orderId=" + orderId + ", userId=" + userId + ", paymentId="
                + paymentId + ", amount=" + amount + ", paymentDate=" + paymentDate + ", paymentTime=" + paymentTime
                + ", isSucess=" + isSucess + "]";
    }
    
}
