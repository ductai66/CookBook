package com.tai06dothe.cookbook.Model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Recipe implements Serializable {
    private String recipeId;
    private String recipeName;
    private String recipeImage;
    private List<String> ingredientList;
    private List<RecipeStep> recipeStepList;
    private String categoryId;
    private String userId;
    private String userName;
    private String createAt;
    private int favoriteNumber;
    private int viewNumber;
    private String status;

    public Recipe() {
    }

    public Recipe(String recipeId, String recipeName, String recipeImage, List<String> ingredientList, List<RecipeStep> recipeStepList,
                  String categoryId, String userId, String userName, String createAt, int favoriteNumber, int viewNumber, String status) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.ingredientList = ingredientList;
        this.recipeStepList = recipeStepList;
        this.categoryId = categoryId;
        this.userId = userId;
        this.userName = userName;
        this.createAt = createAt;
        this.favoriteNumber = favoriteNumber;
        this.viewNumber = viewNumber;
        this.status = status;
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

    public List<String> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<RecipeStep> getRecipeStepList() {
        return recipeStepList;
    }

    public void setRecipeStepList(List<RecipeStep> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(int favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
