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

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getUsera() {
        return usera;
    }

    public void setUsera(String usera) {
        this.usera = usera;
    }

    public String getTimea() {
        return timea;
    }

    public void setTimea(String timea) {
        this.timea = timea;
    }
}
