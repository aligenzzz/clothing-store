package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.entities.Item;
import com.example.test.interfaces.IListener;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable
{
    @FXML
    private Button profileButton;
    @FXML
    private Button favouriteItemsButton;
    @FXML
    private Button shoppingItemsButton;
    @FXML
    private Button ordersButton;
    @FXML
    private Button purchasedItemsButton;
    @FXML
    private Button shopsButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button returnButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private GridPane gridPane;
    private IListener listener;
    private final Customer customer = (Customer) GlobalEntities.USER;
    private GridAnimation animation;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        listener = this::chooseItemCard;
        disactiveButtons();
        try { this.profileButtonOnAction(); }
        catch (IOException e) { throw new RuntimeException(e); }

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

                customer.setFavouriteItems(databaseConnector.getFavouriteItems(customer.getId()));
                customer.setShoppingItems(databaseConnector.getShoppingItems(customer.getId()));
                customer.setPurchasedItems(databaseConnector.getPurchasedItems(customer.getId()));
                customer.setFavouriteShops(databaseConnector.getFavouriteShops(customer.getId()));

                return null;
            }
        };

        profileButton.disableProperty().bind(task.runningProperty());
        favouriteItemsButton.disableProperty().bind(task.runningProperty());
        shoppingItemsButton.disableProperty().bind(task.runningProperty());
        ordersButton.disableProperty().bind(task.runningProperty());
        purchasedItemsButton.disableProperty().bind(task.runningProperty());
        shopsButton.disableProperty().bind(task.runningProperty());
        settingsButton.disableProperty().bind(task.runningProperty());
        returnButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private ImageView imageView;
    @FXML
    private Label itemNameLabel;
    @FXML
    private Label itemPriceLabel;
    @FXML
    private Label itemShopLabel;
    @FXML
    private AnchorPane anchorPane;

    public void chooseItemCard(Item item)
    {
        imageView.setImage(new Image(Constants.ITEMSIMAGEPATH + item.getImageSource()));
        itemNameLabel.setText("☆ " + item.getName().toUpperCase() + " ☆");
        itemPriceLabel.setText(String.valueOf(item.getPrice()));
        itemShopLabel.setText(DatabaseConnector.getInstance().getShop(item.getShop()).getName());
        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }
    @FXML
    Button closeItemCardButton;
    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }

    public void profileButtonOnAction() throws IOException
    {
        if (profileButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(false);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.PROFILE).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.getController();
        profileController.setData();
        stackPane.getChildren().add(anchorPane);

        profileButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void favouriteItemsButtonOnAction()
    {
        if (favouriteItemsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(true);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        animation = new GridAnimation(customer.getFavouriteItems(), gridPane, scrollPane, listener, 4);
        animation.start();
        favouriteItemsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    @FXML
    private VBox vBox;

    public void shoppingItemsButtonOnAction() throws IOException
    {
        if (shoppingItemsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(true);

        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.PAYMENT).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();
        PaymentController paymentController = fxmlLoader.getController();
        paymentController.setData();
        vBox.getChildren().add(anchorPane);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        animation = new GridAnimation(customer.getShoppingItems(), gridPane, scrollPane, listener, 4);
        animation.start();
        shoppingItemsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void ordersButtonOnAction()
    {
        if (ordersButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(false);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        ordersButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void purchasedItemsButtonOnAction()
    {
        if (purchasedItemsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(true);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        animation = new GridAnimation(customer.getPurchasedItems(), gridPane, scrollPane, listener, 4);
        animation.start();
        purchasedItemsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void shopsButtonOnAction()
    {
        if (shopsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(true);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        animation = new GridAnimation(customer.getFavouriteShops(), gridPane, scrollPane, listener, 1);
        animation.start();
        shopsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void settingsButtonOnAction()
    {
        if (settingsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(false);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        settingsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void returnButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.MAINPAGE).toURI().toURL());
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 700));
        stage.centerOnScreen();
    }

    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.close();
    }

    private void disactiveButtons()
    {
        profileButton.setTextFill(Color.WHITE);
        favouriteItemsButton.setTextFill(Color.WHITE);
        shoppingItemsButton.setTextFill(Color.WHITE);
        ordersButton.setTextFill(Color.WHITE);
        purchasedItemsButton.setTextFill(Color.WHITE);
        shopsButton.setTextFill(Color.WHITE);
        settingsButton.setTextFill(Color.WHITE);
        returnButton.setTextFill(Color.WHITE);
    }
}
