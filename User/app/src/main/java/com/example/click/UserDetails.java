package com.example.click;

public class UserDetails {
    public static String username = "";
    public static String email = "";
    public static String chatWith = "";
    public static String photo = "";

    public UserDetails(String username, String photo) {
        this.username = username;
        this.photo = photo;
    }

    public static String getPhoto() {
        return photo;
    }

    public static String getUsername() {
        return username;
    }

}
