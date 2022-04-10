package com.tai06dothe.cookbook.OOP;

import com.tai06dothe.cookbook.Model.Category;
import com.tai06dothe.cookbook.Model.Recipe;

import java.util.List;

public class CategoryRecipes {

    private Category category;
    private List<Recipe> recipes;

    public CategoryRecipes() {
    }

    public CategoryRecipes(Category category, List<Recipe> recipes) {
        this.category = category;
        this.recipes = recipes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
