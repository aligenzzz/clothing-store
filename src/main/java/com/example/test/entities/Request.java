package com.example.test.entities;

import com.example.test.DatabaseConnector;

import java.io.IOException;

public class Request
{
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
    private final double id;
    private final String subject;
    private final String message;

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

    public double getId() { return this.id; }
    public String getSubject() { return this.subject; }
    public String getMessage() { return this.message; }
    public void Send() throws IOException { databaseConnector.addRequest(this); }

    public void Delete()
    {
        // databaseConnector.deleteRequest(this);
    }
}
