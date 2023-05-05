package com.example.test;

import com.example.test.enums.AccessType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LoginHandler
{
    public static void login(@NotNull Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);

        String path = "";
        if (GlobalEntities.USER.accessType == AccessType.nonuser ||
            GlobalEntities.USER.accessType == AccessType.customer)
        {
            fxmlLoader = new FXMLLoader(new File(Constants.MAINPAGE).toURI().toURL());
            scene = new Scene(fxmlLoader.load(), 900, 700);
        }
        else if (GlobalEntities.USER.accessType == AccessType.vendor)
        {
            fxmlLoader = new FXMLLoader(new File(Constants.VENDORPAGE).toURI().toURL());
            scene = new Scene(fxmlLoader.load(), 900, 700);
        }
        else if (GlobalEntities.USER.accessType == AccessType.admin)
        {
            fxmlLoader = new FXMLLoader(new File(Constants.ADMINPAGE).toURI().toURL());
            scene = new Scene(fxmlLoader.load(), 900, 700);
        }

        stage.setTitle("Online clothing store KIΞO");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}