package com.example.test.controllers;

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
        Image image = new Image("D:\\JavaIDE\\test\\src\\main\\resources\\images\\items\\" + item.getImageSource());
        itemImageView.setImage(image);
        itemImageView.setStyle("-fx-background-position: CENTER;");
        priceLabel.setText(item.getPrice());
        this.item = item;

        this.listener = listener;
    }

    public void onMouseClicked(MouseEvent event) { listener.onClickListener(item); }

    public void centerImage()
    {
        Image image = itemImageView.getImage();
        if (image != null)
        {
            double w = 0;
            double h = 0;

            double ratioX = itemImageView.getFitWidth() / image.getWidth();
            double ratioY = itemImageView.getFitHeight() / image.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) reducCoeff = ratioY;
            else reducCoeff = ratioX;

            w = image.getWidth() * reducCoeff;
            h = image.getHeight() * reducCoeff;

            itemImageView.setX((itemImageView.getFitWidth() - w) / 2);
            itemImageView.setY((itemImageView.getFitHeight() - h) / 2);
        }
    }
}
