package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.Shop;
import com.example.test.entities.Vendor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.File;

public class VendorPageController
{
    private Vendor vendor = (Vendor) GlobalEntities.USER;
    @FXML
    private Button shopButton;
    @FXML
    private ScrollPane scrollPane;
    public void shopButtonOnAction()
    {
        GlobalEntities.SHOP = new Shop();
        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
                GlobalEntities.SHOP = databaseConnector.getShop(databaseConnector.getShopId(vendor.getId()));
                return null;
            }
        };
        task.setOnSucceeded(event ->
        {
            try
            {
                Parent root = FXMLLoader.load(new File(Constants.SHOPPAGE).toURI().toURL());
                Stage stage = (Stage) scrollPane.getScene().getWindow();
                stage.setScene(new Scene(root, 900, 700));
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
    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) shopButton.getScene().getWindow();
        stage.close();
    }
}
