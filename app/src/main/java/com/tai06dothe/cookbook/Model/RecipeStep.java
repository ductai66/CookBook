package com.tai06dothe.cookbook.Model;

import java.io.Serializable;

public class RecipeStep implements Serializable {
    private String image;
    private String description;

    public RecipeStep() {
    }

    public RecipeStep(String image, String description) {
        this.image = image;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
