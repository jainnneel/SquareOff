package com.squareoff.dto;

public class UserDto {

    private String userName;
    
    private String userMobie;
    
    private String userPassword;

    public UserDto() {
        super();
    }

    public UserDto(String userName, String userMobie, String userPassword) {
        super();
        this.userName = userName;
        this.userMobie = userMobie;
        this.userPassword = userPassword;
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

    
}
