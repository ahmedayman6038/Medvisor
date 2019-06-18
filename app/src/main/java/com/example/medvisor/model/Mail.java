package com.example.medvisor.model;

public class Mail {
    private String Reciever;
    private String Subject;
    private String Content;

    public Mail(String reciever, String subject, String content) {
        Reciever = reciever;
        Subject = subject;
        Content = content;
    }

    public String getReciever() {
        return Reciever;
    }

    public void setReciever(String reciever) {
        Reciever = reciever;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
