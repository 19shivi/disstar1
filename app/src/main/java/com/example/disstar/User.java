package com.example.disstar;

public class User {
    public String email;
    public String userId;
    public User(){};
    public User(String email,String userId){
        this.email=email;
        this.userId=userId;
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
