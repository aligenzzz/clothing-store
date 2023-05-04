package com.example.test;

import com.example.test.interfaces.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import my.project.test.Serializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application
{
    @Override
    public void start(@NotNull Stage stage) throws IOException
    {
        User user = Serializer.deserialize(User.class, Constants.JSONFILE);
        if (user != null) System.out.print(user.getEmail());

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        stage.setTitle("Online clothing store KIΞO");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    public static void main(String[] args)
    {
        launch();

        User user = new User(GlobalEntities.USER);
        Serializer.serialize(user, Constants.JSONFILE);
    }
}