package com.mobile.utn.quecocino.model;

import java.io.Serializable;

/**
 * Created by Fran on 7/9/2017.
 */

public class Recipe implements Serializable{
    private String idRecipe;
    private String title;
    private String author;
    private int cookingTimeMinutes;
    private String mainImage;
    private String applianceCooking;
    private boolean isPopular;
    private String difficulty;
    private int kcal;

    public Recipe() {
    }

    public Recipe(String idRecipe, String title, String author,
                        int cookingTimeMinutes, String mainImage, String applianceCooking,
                                String difficulty, int kcal) {
        this.idRecipe = idRecipe;
        this.title = title;
        this.author = author;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.mainImage = mainImage;
        this.applianceCooking = applianceCooking;
        this.difficulty = difficulty;
        this.kcal = kcal;
    }

    public String getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(String idRecipe) {
        this.idRecipe = idRecipe;
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

    public String getApplianceCooking() {
        return applianceCooking;
    }

    public void setApplianceCooking(String applianceCooking) {
        this.applianceCooking = applianceCooking;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }
}
