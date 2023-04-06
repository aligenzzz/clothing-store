package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.entities.Shop;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShopController
{
    @FXML
    private ImageView imageView;
    @FXML
    private Label shopNameLabel;
    public void setData(Shop shop)
    {
        Image image = new Image(Constants.SHOPSIMAGEPATH + shop.getImageSource());
        imageView.setImage(image);
        imageView.setStyle("-fx-background-position: CENTER;");
        shopNameLabel.setText(shop.getName());
    }
    public void toShopButtonOnAction()
    {

    }
}
