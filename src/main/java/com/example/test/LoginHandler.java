package com.example.test;

import com.example.test.entities.Admin;
import com.example.test.entities.Customer;
import com.example.test.entities.NonUser;
import com.example.test.entities.Vendor;
import com.example.test.enums.AccessType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class LoginHandler
{
    public static void login(@NotNull Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);

        if (!netIsAvailable())
        {
            fxmlLoader = new FXMLLoader(new File(Constants.CONNECTIONERROR).toURI().toURL());
            scene = new Scene(fxmlLoader.load(), 450, 300);
        }
        else
        {
            DatabaseConnector.getInstance();

            if (GlobalEntities.USER.accessType == AccessType.nonuser)
            {
                GlobalEntities.USER = new NonUser(GlobalEntities.USER);
                fxmlLoader = new FXMLLoader(new File(Constants.MAINPAGE).toURI().toURL());
                scene = new Scene(fxmlLoader.load(), 900, 700);
            }
            else if (GlobalEntities.USER.accessType == AccessType.customer)
            {
                GlobalEntities.USER = new Customer(GlobalEntities.USER);
                fxmlLoader = new FXMLLoader(new File(Constants.MAINPAGE).toURI().toURL());
                scene = new Scene(fxmlLoader.load(), 900, 700);
            }
            else if (GlobalEntities.USER.accessType == AccessType.vendor)
            {
                GlobalEntities.USER = new Vendor(GlobalEntities.USER);
                fxmlLoader = new FXMLLoader(new File(Constants.VENDORPAGE).toURI().toURL());
                scene = new Scene(fxmlLoader.load(), 900, 700);
            }
            else if (GlobalEntities.USER.accessType == AccessType.admin)
            {
                GlobalEntities.USER = new Admin(GlobalEntities.USER);
                fxmlLoader = new FXMLLoader(new File(Constants.ADMINPAGE).toURI().toURL());
                scene = new Scene(fxmlLoader.load(), 900, 700);
            }
        }

        stage.setTitle("Online clothing store KIΞO");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    private static boolean netIsAvailable()
    {
        try
        {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        } catch (IOException exception) {
            return false;
        }
    }
}
