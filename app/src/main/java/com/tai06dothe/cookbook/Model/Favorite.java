package com.tai06dothe.cookbook.Model;

import java.io.Serializable;
import java.util.List;

public class Favorite implements Serializable {
    private String favoriteId;
    private String recipeId;
    private String recipeName;
    private String recipeImage;
    private List<String> ingredientList;
    private List<RecipeStep> recipeStepList;
    private String categoryId;
    private String createById;
    private String userId;
    private boolean status;

    public Favorite() {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
