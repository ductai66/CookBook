package com.tai06dothe.cookbook.Model;

import java.io.Serializable;

public class Comment implements Serializable {
    private int id;
    private String content;
    private String image;
    private int userId;
    private int recipeId;

    public Comment() {
    }

    public Comment(int id, String content, String image, int userId, int recipeId) {
        this.id = id;
        this.content = content;
        this.image = image;
        this.userId = userId;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
