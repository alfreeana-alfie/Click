package com.example.click.data;

public class User {
    public String username;
    public String photo;

    public User(String username, String photo) {
        this.username = username;
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
