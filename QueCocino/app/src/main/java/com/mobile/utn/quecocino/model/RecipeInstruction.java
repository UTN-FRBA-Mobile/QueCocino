package com.mobile.utn.quecocino.model;

/**
 * Created by Fran on 30/9/2017.
 */

public class RecipeInstruction {
    private String description;
    private boolean checked;
    private String time;
    private String tag;

    public RecipeInstruction() {
        checked = false;
    }

    public RecipeInstruction(String description, boolean checked, String time, String tag) {
        this.description = description;
        this.checked = checked;
        this.time = time;
        this.tag = tag;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
