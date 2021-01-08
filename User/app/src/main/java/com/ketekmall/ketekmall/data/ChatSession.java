package com.ketekmall.ketekmall.data;

public class ChatSession {
    private String  ID;
    private String Name;
    private String UserPhoto;
    private String UserID;
    private String ChatWith;
    private String ChatWithID;
    private String ChatWithPhoto;
    private String Content;
    private String CreatedDateTime;

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public String getChatCount() {
        return ChatCount;
    }

    public void setChatCount(String chatCount) {
        ChatCount = chatCount;
    }

    private String ChatCount;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getChatWith() {
        return ChatWith;
    }

    public void setChatWith(String chatWith) {
        ChatWith = chatWith;
    }

    public String getChatWithID() {
        return ChatWithID;
    }

    public void setChatWithID(String chatWithID) {
        ChatWithID = chatWithID;
    }

    public String getChatWithPhoto() {
        return ChatWithPhoto;
    }

    public void setChatWithPhoto(String chatWithPhoto) {
        ChatWithPhoto = chatWithPhoto;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }
}
