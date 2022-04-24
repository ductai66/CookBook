package com.tai06dothe.cookbook.Model;

import java.io.Serializable;

public class Notification implements Serializable {

    private String title;
    private String recipeId; // id of Recipe
    private String userId; // nguoi them cong thuc moi hoac coment
    private String userName;

    public Notification() {
    }

    public Notification(String title, String recipeId, String userId, String userName) {
        this.title = title;
        this.recipeId = recipeId;
        this.userId = userId;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
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
}
