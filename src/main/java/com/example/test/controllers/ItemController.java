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
        String imagePath = "";
        if (item.getImageSource().startsWith("https://"))
            imagePath = item.getImageSource();
        else
            imagePath = Constants.ITEMSIMAGEPATH + item.getImageSource();

        Image image = new Image(imagePath);
        itemImageView.setImage(image);
        itemImageView.setStyle("-fx-background-position: CENTER;");
        priceLabel.setText(Constants.PRICE_FORMAT.format(item.getPrice()) + " $");
        this.item = item;

        this.listener = listener;
    }

    public void onMouseClicked() throws IOException { listener.onClickListener(item); }
}
