package com.mobile.utn.quecocino.UploadImages.Objects;

import java.io.Serializable;

/**
 * Created by Fran on 7/9/2017.
 */

public class Recipe implements Serializable {
    String nombre;
    String autor;
    int duracionEnMinutos;
    String url;

    public Recipe() {
    }

    public Recipe(String nombre, String autor, int duracionEnMinutos, String url) {
        this.nombre = nombre;
        this.autor = autor;
        this.duracionEnMinutos = duracionEnMinutos;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getDuracionEnMinutos() {
        return duracionEnMinutos;
    }

    public void setDuracionEnMinutos(int duracionEnMinutos) {
        this.duracionEnMinutos = duracionEnMinutos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
