package com.mobile.utn.quecocino.model;

import java.io.Serializable;

/**
 * Created by Fran on 7/9/2017.
 */

public class Recipe implements Serializable{
    private String title;
    private String author;
    private int cookingTimeMinutes;
    private String mainImage;

    public Recipe() {
    }

    public Recipe(String title, String author, int cookingTimeMinutes, String mainImage) {
        this.title = title;
        this.author = author;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.mainImage = mainImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCookingTimeMinutes() {
        return cookingTimeMinutes;
    }

    public void setCookingTimeMinutes(int cookingTimeMinutes) {
        this.cookingTimeMinutes = cookingTimeMinutes;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
}
