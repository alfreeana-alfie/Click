package com.ketekmall.ketekmall.data;

public class Chat_Detail {
    public  String messages;
    public String usera;
    public  String timea;

    public Chat_Detail(String messages, String usera, String timea) {
        this.messages = messages;
        this.usera = usera;
        this.timea = timea;
    }

    public String getMessages() {
        return messages;
    }

    public String getUsera() {
        return usera;
    }

    public String getTimea() {
        return timea;
    }

}
