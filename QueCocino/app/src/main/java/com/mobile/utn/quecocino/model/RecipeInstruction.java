package com.mobile.utn.quecocino.model;

/**
 * Created by Fran on 30/9/2017.
 */

public class RecipeInstruction {
    private String description;
    private boolean checked;

    public RecipeInstruction() {
        checked = false;
    }

    public RecipeInstruction(String description, boolean checked) {
        this.description = description;
        this.checked = checked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
