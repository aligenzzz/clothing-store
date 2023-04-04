package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.entities.Item;
import com.example.test.interfaces.IListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemController
{
    @FXML
    ImageView itemImageView;
    @FXML
    Label priceLabel;
    Item item;
    private IListener listener;

    public void setData(Item item, IListener listener)
    {
        Image image = new Image(Constants.ITEMSIMAGEPATH + item.getImageSource());
        itemImageView.setImage(image);
        itemImageView.setStyle("-fx-background-position: CENTER;");
        priceLabel.setText(String.valueOf(item.getPrice()));
        this.item = item;

        this.listener = listener;
    }

    public void onMouseClicked(MouseEvent event) { listener.onClickListener(item); }
}
