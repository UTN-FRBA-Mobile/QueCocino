package com.mobile.utn.quecocino.model;

/**
 * Created by Fran on 30/9/2017.
 */

public class RecipeIngredient {

    private String description;

    public RecipeIngredient() {
    }

    public RecipeIngredient(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
