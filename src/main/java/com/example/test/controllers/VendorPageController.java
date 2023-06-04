package com.example.test.controllers;

import com.example.test.*;
import com.example.test.entities.Vendor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class VendorPageController implements Initializable
{
    private final Vendor vendor = (Vendor) GlobalEntities.USER;

    @FXML private Button shopButton;
    @FXML private ScrollPane scrollPane;
    @FXML private Button profileButton;
    @FXML private Button ordersButton;
    @FXML private Button addItemButton;
    @FXML private Button requestsButton;
    @FXML private Button settingsButton;
    @FXML private StackPane stackPane;
    @FXML private GridPane gridPane;

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

        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

        Task<Void> task1 = new Task<>()
        {
            @Override
            protected Void call() throws IOException
            {
                vendor.setOrders(databaseConnector.getVendorOrders(vendor.getId()));
                return null;
            }
        };
        ordersButton.disableProperty().bind(task1.runningProperty());

        Task<Void> task2 = new Task<>()
        {
            @Override
            protected Void call() throws IOException
            {
                vendor.setShop(databaseConnector.getShop(databaseConnector.getShopId(vendor.getId())));
                return null;
            }
        };
        task2.setOnSucceeded(event -> GlobalEntities.SHOP = vendor.getShop());
        addItemButton.disableProperty().bind(task2.runningProperty());
        shopButton.disableProperty().bind(task2.runningProperty());

        Thread thread1 = new Thread(task1);
        thread1.setDaemon(true);
        thread1.start();

        Thread thread2 = new Thread(task2);
        thread2.setDaemon(true);
        thread2.start();
    }

    public void profileButtonOnAction() throws IOException { this.showSimplePanel(profileButton, Constants.PROFILE); }
    public void shopButtonOnAction() throws IOException
    {
        if (vendor.getShop() == null) return;

        if (stackPane.getChildren().size() == 2) stackPane.getChildren().remove(1);

        Parent root = FXMLLoader.load(new File(Constants.SHOPPAGE).toURI().toURL());
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 700));
        stage.centerOnScreen();
    }

    private GridAnimation animation;
    public void ordersButtonOnAction()
    {
        if (ordersButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(true);

        if (stackPane.getChildren().size() == 2) stackPane.getChildren().remove(1);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        animation = new GridAnimation(vendor.getOrders(), gridPane, scrollPane, null, 2);
        animation.start();

        ordersButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void addItemButtonOnAction() throws IOException
    {
        if (vendor.getShop() == null) return;

        FXMLLoader loader = new FXMLLoader(new File(Constants.EDITITEM).toURI().toURL());
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 350, 300));
        stage.setTitle("Edit item");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.centerOnScreen();
    }

    public void requestsButtonOnAction() throws IOException { this.showSimplePanel(requestsButton, Constants.REQUESTSPAGE); }
    public void settingsButtonOnAction() throws IOException { this.showSimplePanel(settingsButton, Constants.SETTINGS); }

    public void logoutMenuButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 400));
        stage.centerOnScreen();
    }
    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) shopButton.getScene().getWindow();
        stage.close();
    }

    private void disactiveButtons()
    {
        profileButton.setTextFill(Constants.DISACTIVECOLOR);
        shopButton.setTextFill(Constants.DISACTIVECOLOR);
        ordersButton.setTextFill(Constants.DISACTIVECOLOR);
        addItemButton.setTextFill(Constants.DISACTIVECOLOR);
        requestsButton.setTextFill(Constants.DISACTIVECOLOR);
        settingsButton.setTextFill(Constants.DISACTIVECOLOR);
    }
    private void showSimplePanel(@NotNull Button button, String path) throws IOException
    {
        scrollPane.setVisible(false);

        if (button.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if (stackPane.getChildren().size() == 2) stackPane.getChildren().remove(1);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(path).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();

        if (button == profileButton)
        {
            ProfileController profileController = fxmlLoader.getController();
            profileController.setData();
        }

        stackPane.getChildren().add(anchorPane);

        button.setTextFill(Constants.ACTIVECOLOR);
    }
}
