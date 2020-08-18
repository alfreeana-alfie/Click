package com.example.click;

public class promotion {

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

    public promotion(String[] photo) {
        this.photo = photo;
    }
}
