package com.mobile.utn.quecocino.model;

/**
 * Created by Fran on 30/9/2017.
 */

public class RecipeInstruction {
    private String description;
    private boolean checked;
    private int time;

    public RecipeInstruction() {
        checked = false;
    }

    public RecipeInstruction(String description, boolean checked, int time) {
        this.description = description;
        this.checked = checked;
        this.time = time;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
