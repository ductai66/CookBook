package com.tai06dothe.cookbook.Model;

public class RecipeStep {
    private String id;
    private String image;
    private String description;

    public RecipeStep() {
    }

    public RecipeStep(String image, String description) {
        this.image = image;
        this.description = description;
    }

    public RecipeStep(String id, String image, String description) {
        this.id = id;
        this.image = image;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
