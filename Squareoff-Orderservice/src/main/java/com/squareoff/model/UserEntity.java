package com.squareoff.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserEntity {

    /**
     * 
     */

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    
    private String userName;
    
    private String userMobie;
    
    @JsonIgnore
    private String userPassword;
    
    private BigDecimal userWallet;
    
    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserHoldingModel> holding;
    
    @JsonIgnore
    private boolean userEnable;

    public UserEntity() {
        super();
    }

    public UserEntity(String userId, String userName, String userMobie, String userPassword, boolean userEnable) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userMobie = userMobie;
        this.userPassword = userPassword;
        this.userEnable = userEnable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean getUserEnable() {
        return userEnable;
    }

    public void setUserEnable(boolean userEnable) {
        this.userEnable = userEnable;
    }

    public BigDecimal getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(BigDecimal userWallet) {
        this.userWallet = userWallet;
    }
    
    public List<UserHoldingModel> getHolding() {
        return holding;
    }

    public void setHolding(List<UserHoldingModel> holding) {
        this.holding = holding;
    }

    @Override
    public String toString() {
        return "UserEntity [userId=" + userId + ", userName=" + userName + ", userMobie=" + userMobie
                + ", userPassword=" + userPassword + ", userEnable=" + userEnable + "]";
    }
}
