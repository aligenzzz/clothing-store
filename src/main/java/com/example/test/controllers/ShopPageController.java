package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.entities.Item;
import com.example.test.entities.NonUser;
import com.example.test.entities.Vendor;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.IListener;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShopPageController implements Initializable
{
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox customerButtons;
    @FXML
    private HBox vendorButtons;
    @FXML
    private ImageView bannerImageView;
    @FXML
    private Label shopNameLabel;

    private IListener listener;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        listener = this::chooseItemCard;

        if (GlobalEntities.USER instanceof Customer || GlobalEntities.USER instanceof NonUser) customerButtons.setVisible(true);
        if (GlobalEntities.USER instanceof Vendor) vendorButtons.setVisible(true);

        bannerImageView.setImage(new Image(Constants.SHOPSIMAGEPATH + GlobalEntities.SHOP.getImageSource()));
        shopNameLabel.setText(GlobalEntities.SHOP.getName());
        shopNameLabel.setTextFill(GlobalEntities.SHOP.getTextColor());
        List<Item> itemList = GlobalEntities.SHOP.getItems();

        ShopPageController.GridAnimation animation = new ShopPageController.GridAnimation(itemList);
        animation.start();
    }

    @FXML
    ImageView imageView;
    @FXML
    Label itemNameLabel;
    @FXML
    Label itemPriceLabel;
    @FXML
    AnchorPane anchorPane;

    private double itemId;
    public void chooseItemCard(Item item)
    {
        this.itemId = item.getId();

        imageView.setImage(new Image(Constants.ITEMSIMAGEPATH + item.getImageSource()));
        itemNameLabel.setText("☆ " + item.getName().toUpperCase() + " ☆");
        itemPriceLabel.setText(String.valueOf(item.getPrice()));

        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }
    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }

    public void toFavouriteButtonOnAction()
    {
        if (GlobalEntities.USER.accessType == AccessType.nonuser) return;

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.addFavouriteItem(GlobalEntities.USER.id, this.itemId);
    }
    public void toShoppingButtonOnAction()
    {
        if (GlobalEntities.USER.accessType == AccessType.nonuser) return;

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.addShoppingItem(GlobalEntities.USER.id, this.itemId);
    }
    public void returnMenuButtonOnAction()
    {
        String path = "";
        if (GlobalEntities.USER instanceof Customer || GlobalEntities.USER instanceof NonUser) path = Constants.MAINPAGE;
        if (GlobalEntities.USER instanceof Vendor) path = Constants.VENDORPAGE;

        try
        {
            Parent root = FXMLLoader.load(new File(path).toURI().toURL());
            Stage stage = (Stage) scrollPane.getScene().getWindow();
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
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.close();
    }

    private class GridAnimation extends AnimationTimer
    {
        private final List<Item> itemList;
        private int size = 0;
        private int i = 0;
        int column = 0;
        int row = 0;
        public GridAnimation(List<Item> itemList)
        {
            this.itemList = itemList;
            if (itemList != null) this.size = itemList.size();
        }
        @Override
        public void handle(long now)
        {
            scrollPane.setHvalue(0);
            scrollPane.setVvalue(0);

            if (this.itemList == null) return;

            try { doHandle(); }
            catch (IOException e) { throw new RuntimeException(e); }

            gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxHeight(Region.USE_PREF_SIZE);
        }

        private void doHandle() throws IOException
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File(Constants.ITEM).toURI().toURL());
            AnchorPane anchorPane = fxmlLoader.load();

            Item item = itemList.get(i);
            if (column == 6)
            {
                column = 0;
                row++;
            }

            ItemController itemController = fxmlLoader.getController();
            itemController.setData(item, listener);
            gridPane.add(anchorPane, column++, row);

            GridPane.setMargin(anchorPane, new Insets(10));

            i++;
            if (i >= size) stop();
        }
    }
}
