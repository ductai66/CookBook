package com.tai06dothe.cookbook.Model;

import java.io.Serializable;

public class Favorite implements Serializable {

    private String favoriteId;
    private String userId;
    private String recipeId;
    private String recipeName;
    private String recipeImage;
    private String createById;

    public Favorite() {
    }

    public Favorite(String favoriteId, String userId, String recipeId, String recipeName, String recipeImage, String createById) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.createById = createById;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
