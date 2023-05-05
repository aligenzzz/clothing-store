package com.example.test;

import com.example.test.interfaces.User;
import javafx.application.Application;
import javafx.stage.Stage;
import my.project.test.Serializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Main extends Application
{
    @Override
    public void start(@NotNull Stage stage) throws IOException
    {
        User user = Serializer.deserialize(User.class, Constants.JSONFILE);
        if (user != null) GlobalEntities.USER = user;
        else GlobalEntities.USER = new User();

        LoginHandler.login(stage);
    }
    public static void main(String[] args)
    {
        launch();

        User user = new User(GlobalEntities.USER);
        Serializer.serialize(user, Constants.JSONFILE);
    }
}