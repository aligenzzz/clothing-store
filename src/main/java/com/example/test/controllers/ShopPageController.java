package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.GridAnimation;
import com.example.test.entities.Customer;
import com.example.test.entities.Item;
import com.example.test.entities.NonUser;
import com.example.test.entities.Vendor;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.IListener;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShopPageController implements Initializable
{
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private HBox customerButtons;
    @FXML private HBox vendorButtons;
    @FXML private ImageView bannerImageView;
    @FXML private Label shopNameLabel;
    @FXML private Menu returnMenuItem;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        alert.setHeaderText("You have no rights to this action!");
        alert.setContentText("Do yo want signup or login???");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setBorder(Border.stroke(Color.web("#b56d8c")));
        alert.setOnCloseRequest(event ->
        {
            ButtonType result = alert.getResult();
            if (result == ButtonType.OK)
            {
                try
                {
                    Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
                    Stage stage = (Stage) gridPane.getScene().getWindow();
                    stage.setScene(new Scene(root, 520, 400));
                    stage.centerOnScreen();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                    exception.getCause();
                }
            }
        });

        IListener listener = this::chooseItemCard;

        if (GlobalEntities.USER instanceof Customer || GlobalEntities.USER instanceof NonUser) customerButtons.setVisible(true);
        if (GlobalEntities.USER instanceof Vendor)
        {
            vendorButtons.setVisible(true);
            returnMenuItem.setVisible(true);
        }

        bannerImageView.setImage(new Image(Constants.SHOPSIMAGEPATH + GlobalEntities.SHOP.getImageSource()));
        shopNameLabel.setText(GlobalEntities.SHOP.getName().toUpperCase());
        shopNameLabel.setTextFill(GlobalEntities.SHOP.getTextColor());
        List<Item> itemList = GlobalEntities.SHOP.getItems();

        GridAnimation animation = new GridAnimation(itemList, gridPane, scrollPane, listener, 5);
        animation.start();
    }
    @FXML private ImageView imageView;
    @FXML private Label itemNameLabel;
    @FXML private Label itemPriceLabel;
    @FXML private AnchorPane anchorPane;
    private double itemId;
    public void chooseItemCard(@NotNull Item item)
    {
        this.itemId = item.getId();

        imageView.setImage(new Image(Constants.ITEMSIMAGEPATH + item.getImageSource()));
        itemNameLabel.setText("☆ " + item.getName().toUpperCase() + " ☆");
        itemPriceLabel.setText(Constants.PRICE_FORMAT.format(item.getPrice()) + " $");

        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }
    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }
    public void toFavouriteButtonOnAction() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Customer customer = (Customer) GlobalEntities.USER;
        if (customer.getFavouriteItems() == null)
            DatabaseConnector.getInstance().addFavouriteItem(customer.getId(), this.itemId);
        else
            customer.addFavouriteItem(this.itemId);
    }
    public void toShoppingButtonOnAction() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Customer customer = (Customer) GlobalEntities.USER;
        if (customer.getShoppingItems() == null)
            DatabaseConnector.getInstance().addShoppingItem(customer.getId(), this.itemId);
        else
            customer.addShoppingItem(this.itemId);
    }
    public void returnMenuButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.VENDORPAGE).toURI().toURL());
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 700));
        stage.centerOnScreen();
    }
    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.close();
    }
}
