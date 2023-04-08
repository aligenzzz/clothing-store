package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.entities.Item;
import com.example.test.interfaces.IListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ItemController
{
    @FXML private ImageView itemImageView;
    @FXML private Label priceLabel;
    private Item item;
    private IListener listener;

    public void setData(@NotNull Item item, IListener listener)
    {
        Image image = new Image(Constants.ITEMSIMAGEPATH + item.getImageSource());
        itemImageView.setImage(image);
        itemImageView.setStyle("-fx-background-position: CENTER;");
        priceLabel.setText(Constants.FORMAT.format(item.getPrice()) + " $");
        this.item = item;

        this.listener = listener;
    }

    public void onMouseClicked() throws IOException { listener.onClickListener(item); }
}
