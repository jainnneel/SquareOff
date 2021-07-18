package com.squareoff.dto;

import java.math.BigDecimal;
import java.util.List;

public class UserinfoDto {

private Long userId;
    
    private String userName;
    
    private String userMobie;
    
    private BigDecimal userWallet;
    
    List<UseroldingDto> userholding;

    public UserinfoDto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UserinfoDto(Long userId, String userName, String userMobie, BigDecimal userWallet,
            List<UseroldingDto> userholding) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userMobie = userMobie;
        this.userWallet = userWallet;
        this.userholding = userholding;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobie() {
        return userMobie;
    }

    public void setUserMobie(String userMobie) {
        this.userMobie = userMobie;
    }

    public BigDecimal getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(BigDecimal userWallet) {
        this.userWallet = userWallet;
    }

    public List<UseroldingDto> getUserholding() {
        return userholding;
    }

    public void setUserholding(List<UseroldingDto> userholding) {
        this.userholding = userholding;
    }

    @Override
    public String toString() {
        return "UserinfoDto [userId=" + userId + ", userName=" + userName + ", userMobie=" + userMobie + ", userWallet="
                + userWallet + ", userholding=" + userholding + "]";
    } 
    
    
}
