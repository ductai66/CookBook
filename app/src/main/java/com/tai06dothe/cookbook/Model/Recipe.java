package com.tai06dothe.cookbook.Model;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private String recipeId;
    private String recipeName;
    private String recipeImage;
    private List<String> ingredientList;
    private List<RecipeStep> recipeStepList;
    private String categoryId;
    private String userId;
    private int favoriteNumber;

    public Recipe() {
    }

    public Recipe(String recipeId, String recipeName, String recipeImage, List<String> ingredientList, List<RecipeStep> recipeStepList, String categoryId, String userId, int favoriteNumber) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.ingredientList = ingredientList;
        this.recipeStepList = recipeStepList;
        this.categoryId = categoryId;
        this.userId = userId;
        this.favoriteNumber = favoriteNumber;
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

    public int getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(int favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }
}
