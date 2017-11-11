package com.mobile.utn.quecocino.model;

/**
 * Created by Fran on 28/9/2017.
 */

public class RecipeImage {
    private String author;
    private String url;

    public RecipeImage() {
    }

    public RecipeImage(String author, String url) {
        this.author = author;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
