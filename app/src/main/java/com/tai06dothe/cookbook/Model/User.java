package com.tai06dothe.cookbook.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String userName;
    private String password;
    private String email;
    private String name;
    private String avatar;
    private String role;

    public User() {
    }

    public User(String userId, String userName, String password, String email, String name, String avatar, String role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
