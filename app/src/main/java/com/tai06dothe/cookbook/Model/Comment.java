package com.tai06dothe.cookbook.Model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable {

    private String commentId;
    private String content;
    private String image;
    private String userId;
    private String recipeId;
    private String createAt;
    private String status;

    public Comment() {
    }

    public Comment(String commentId, String content, String image, String userId,
                   String recipeId, String createAt, String status) {
        this.commentId = commentId;
        this.content = content;
        this.image = image;
        this.userId = userId;
        this.recipeId = recipeId;
        this.createAt = createAt;
        this.status = status;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
