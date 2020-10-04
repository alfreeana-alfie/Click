package com.ketekmall.ketekmall.data;

public class User {
    public String username;
    public String photo;
    public String count;
    String chatwith;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getChatwith() {
        return chatwith;
    }

    public void setChatwith(String chatwith) {
        this.chatwith = chatwith;
    }

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
