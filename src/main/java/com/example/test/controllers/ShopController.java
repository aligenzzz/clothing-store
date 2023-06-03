package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Customer;
import com.example.test.entities.Shop;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ShopController
{
    @FXML private Label shopNameLabel;
    private Shop shop;
    public void setData(@NotNull Shop shop)
    {
        shopNameLabel.setText(shop.getName());
        shopNameLabel.setTextFill(shop.getTextColor());
        shopNameLabel.setOpacity(0.8);

        this.shop = shop;
    }
    public void toShopButtonOnAction()
    {
        GlobalEntities.SHOP = this.shop;

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
                GlobalEntities.SHOP = databaseConnector.getShop(GlobalEntities.SHOP.getId());
                return null;
            }
        };
        task.setOnSucceeded(event ->
        {
            try
            {
                Parent root = FXMLLoader.load(new File(Constants.SHOPPAGE).toURI().toURL());
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 900, 700));
                stage.setTitle("Shop");
                stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                stage.centerOnScreen();

            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                exception.getCause();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteButtonOnAction() throws IOException
    {
        Customer customer = (Customer) GlobalEntities.USER;
        customer.deleteFavouriteShop(shop.getId());
    }
}
