package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.Shop;
import com.example.test.entities.Vendor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VendorPageController implements Initializable
{
    private final Vendor vendor = (Vendor) GlobalEntities.USER;
    @FXML private Button shopButton;
    @FXML private ScrollPane scrollPane;
    @FXML private Button profileButton;
    @FXML private Button ordersButton;
    @FXML private Button settingsButton;
    @FXML private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        disactiveButtons();
        try { this.profileButtonOnAction(); }
        catch (IOException exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call() throws IOException
            {
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
                vendor.setOrders(databaseConnector.getVendorOrders(vendor.getId()));

                return null;
            }
        };

        profileButton.disableProperty().bind(task.runningProperty());
        ordersButton.disableProperty().bind(task.runningProperty());
        settingsButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    public void profileButtonOnAction() throws IOException
    {
        if (profileButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.PROFILE).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.getController();
        profileController.setData();
        stackPane.getChildren().add(anchorPane);

        profileButton.setTextFill(Constants.ACTIVECOLOR);
    }
    public void shopButtonOnAction()
    {
        if (stackPane.getChildren().size() == 2) stackPane.getChildren().remove(1);

        GlobalEntities.SHOP = new Shop();
        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException {
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

    private void disactiveButtons()
    {
        profileButton.setTextFill(Color.WHITE);
        shopButton.setTextFill(Color.WHITE);
        ordersButton.setTextFill(Color.WHITE);
        settingsButton.setTextFill(Color.WHITE);
    }

    public void ordersButtonOnAction() throws IOException
    {

    }
}
