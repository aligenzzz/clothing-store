package com.example.test.entities;

public class Request
{
    private double id;
    private String subject;
    private String message;

    public Request(String subject, String message)
    {
        this.id = -1;
        this.subject = subject;
        this.message = message;
    }

    public Request(double id, String subject, String message)
    {
        this.id = id;
        this.subject = subject;
        this.message = message;
    }

    public void Delete()
    {

    }
}
