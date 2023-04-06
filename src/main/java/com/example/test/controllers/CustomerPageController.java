package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.entities.Item;
import com.example.test.entities.Shop;
import com.example.test.interfaces.IListener;

import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
    private GridPane gridPane;
    private IListener listener;
    Customer customer = (Customer) GlobalEntities.USER;

    GridAnimation animation;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        listener = this::chooseItemCard;
        disactiveButtons();
        profileButton.setTextFill(Constants.ACTIVECOLOR);

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
        // !!!!!!
        itemShopLabel.setText("shop");
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

    public void profileButtonOnAction()
    {
        if (profileButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
    }

    public void favouriteItemsButtonOnAction()
    {
        if (favouriteItemsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);

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
    }

    public void purchasedItemsButtonOnAction()
    {
        if (purchasedItemsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);

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

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        GridShopsAnimation animation_ = new GridShopsAnimation(customer.getFavouriteShops());
        animation_.start();
        shopsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void settingsButtonOnAction()
    {
        if (settingsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
    }

    public void returnButtonOnAction()
    {
        try
        {
            Parent root = FXMLLoader.load(new File(Constants.MAINPAGE).toURI().toURL());
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.centerOnScreen();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
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

    public class GridShopsAnimation extends AnimationTimer {
        private final List<Shop> shopList;
        private int size = 0;
        private int i = 0;
        private int column = 0;
        private int row = 0;

        public GridShopsAnimation(List<Shop> shopList) {
            this.shopList = shopList;
            if (shopList != null) this.size = shopList.size();
        }

        @Override
        public void handle(long now) {
            scrollPane.setHvalue(0);
            scrollPane.setVvalue(0);

            if (this.shopList == null) return;

            try {
                doHandle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxHeight(Region.USE_PREF_SIZE);
        }

        private void doHandle() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File(Constants.SHOP).toURI().toURL());
            AnchorPane anchorPane = fxmlLoader.load();

            Shop shop = shopList.get(i);
            if (column == 1)
            {
                column = 0;
                row++;
            }

            ShopController shopController = fxmlLoader.getController();
            shopController.setData(shop);
            gridPane.add(anchorPane, column++, row);

            GridPane.setMargin(anchorPane, new Insets(10));

            i++;
            if (i >= size) stop();
        }
    }
}
