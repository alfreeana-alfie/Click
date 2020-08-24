package com.example.click.data;

public class Promotion {

    String id;
    String[] photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getPhoto() {
        return photo;
    }

    public void setPhoto(String[] photo) {
        this.photo = photo;
    }

    public Promotion(String[] photo) {
        this.photo = photo;
    }
}
